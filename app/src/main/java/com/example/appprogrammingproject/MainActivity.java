package com.example.appprogrammingproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPhotoResponse;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Button btnSettings;
    Button Go;
    private PlacesClient placesClient;


    double addresslatitude =  0.0;
    double addresslongitude = 0.0;

    LatLng addressCoordinates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSettings = (Button) findViewById(R.id.settings_button);
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, PreferencesActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                //restaurantFacilitator(40.1998, -76.7311,10000);
                restaurantFacilitator(addresslatitude, addresslongitude,1000);
            }
        });


        /**
         * Places API additions
         */

        final String api_key = "AIzaSyBsXLFj2Fyy2ceJIQh_sVG20PpCH7aU5dI";
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), api_key, Locale.US);
        }

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                addressCoordinates = place.getLatLng();
                addresslatitude = addressCoordinates.latitude;
                addresslongitude = addressCoordinates.longitude;

                System.out.println("ADDRESS INPUT INFO!" + place.getId() + " " + place.getName());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.

            }
        });

        Go = (Button) findViewById(R.id.button);

        Go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                //restaurantFacilitator(40.1998, -76.7311,10000);
                restaurantFacilitator(addresslatitude, addresslongitude,1000);
            }
        });


        /**
         * place holder for running http call on main thread
         */
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        /**
         * Since a new address will be added every time make sure to wipe the DB
         */
        clearDB();


    }


    private void placeDetails(){
        // Define a Place ID.
        String placeId = "ChIJc-Y-5ka8yIkRNUF3kvJ8NqI";

        // Specify the fields to return.
        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME);

        // Construct a request object, passing the place ID and fields array.
        FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, placeFields);

        placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
            Place place = response.getPlace();
            Log.i("TAG", "Place found: " + place.getName());
            System.out.println("PLACE NAME: " + place.getName());
        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                int statusCode = apiException.getStatusCode();
                // Handle error with given status code.
                Log.e("TAG", "Place not found: " + exception.getMessage());
            }
        });
    }


    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";

    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String TYPE_DETAILS = "/details";
    private static final String TYPE_SEARCH = "/nearbysearch";
    private static final String OUT_JSON = "/json?";
    private static final String LOG_TAG = "ListRest";

    public ArrayList<Restaurant> restaurantFacilitator(double lat, double lng, int radius) {
        ArrayList<Restaurant> resultList = null;
        final String api_key = "AIzaSyBsXLFj2Fyy2ceJIQh_sVG20PpCH7aU5dI";
        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE);
            sb.append(TYPE_SEARCH);
            sb.append(OUT_JSON);
            sb.append("location=" + String.valueOf(lat) + "," + String.valueOf(lng));
            sb.append("&radius=" + String.valueOf(radius));
            sb.append("&type=restaurant");
            sb.append("&key=" + api_key);

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("results");

            // Extract the descriptions from the results
            resultList = new ArrayList<Restaurant>(predsJsonArray.length());


            String name;
            String address;
            String rating;
            String restaurantid;
            for (int i = 0; i < predsJsonArray.length(); i++) {


                name = predsJsonArray.getJSONObject(i).getString("name");
                address = predsJsonArray.getJSONObject(i).getString("vicinity");
                if((predsJsonArray.getJSONObject(i).getString("rating"))== null) {
                    rating = "Not found";
                }
                else {
                    rating = predsJsonArray.getJSONObject(i).getString("rating");
                }
                restaurantid = predsJsonArray.getJSONObject(i).getString("place_id");
                Restaurant restaurant = new Restaurant(name, address, rating);
                populateDB(restaurant);
                resultList.add(restaurant);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error processing JSON results", e);
        }

        return resultList;
      //  return null;
    }


//    private void displayList() {
//        ArrayList<Restaurant> arrayList = restaurantFacilitator(40.1998, -76.7311,10000);
//        for(Restaurant place: arrayList) {
//            System.out.println(place.name);
//        }
//    }

    private void populateDB(Restaurant restaurant){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Create a new user with a first and last name
        Map<String, Object> restaurantObject = new HashMap<>();
        restaurantObject.put("restaurant name", restaurant.getName());
        restaurantObject.put("restaurant address", restaurant.getAddress());
        restaurantObject.put("rating", restaurant.getRating());



        // Add a new document with a generated ID
        db.collection("restaurants").add(restaurant);

    }

    /**
     * Clear the database after someone changes address
     */
    private void clearDB(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("restaurants")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        int restaurants = 0;
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                restaurants++;
                                if(restaurants > 1) {
                                    document.getReference().delete();
                                }
                            }
                        } else {

                        }
                    }
                });
    }

}
