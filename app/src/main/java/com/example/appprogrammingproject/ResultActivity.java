package com.example.appprogrammingproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ResultActivity extends AppCompatActivity {

    TextView Result;
    Button TryAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);

        TryAgain = (Button) findViewById(R.id.button3);

        TryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

            }
        });
        /**
         * get random restaurant index
         */
        int randomRestaurantIndex = 1;

        /**
         * Count the restaurants in the DB
         * select random one
         * generate card
         */
        countCollections();

//        CardView cardView = findViewById(R.id.cardView);
//        Animation myanim = AnimationUtils.loadAnimation(this,R.anim.slide_in_left);
//        cardView.startAnimation(myanim);

        /**
         * Button on click listener
         */
        Button selectRestaurantButton = findViewById(R.id.selectbutton);
        selectRestaurantButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TextView restaurantnametextview = findViewById(R.id.restaurantname);
                TextView restaurantaddress = findViewById(R.id.addressfillin);
                TextView restaurantrating = findViewById(R.id.ratingfillin);
                String name = restaurantnametextview.getText().toString();
                String address = restaurantaddress.getText().toString();
                String rating = restaurantrating.getText().toString();
                populateHistory(name, address, rating);

                Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
                startActivity(intent);

            }
        });

    }

    private void countCollections(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        /**
         * count the number of values in the collection
         */


        db.collection("restaurants")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        int restaurants = 0;
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                restaurants++;
                            }
                            System.out.println("NUMBER OF RESTAURaNTS " + restaurants);
                            generateCard(restaurants);
                        } else {

                        }

                    }

                });


    }


    /**
     * generate selected restaurant card
     * @param numberofrestaurants
     */
    private void generateCard(int numberofrestaurants) {
        /**
         * count the number of values in the collection
         */


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("restaurants")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    TextView restaurantnametextview = findViewById(R.id.restaurantname);
                    TextView addresstextview = findViewById(R.id.addressfillin);
                    TextView ratingtextview = findViewById(R.id.ratingfillin);
                    Random random = new Random();
                    int decidedcollection = random.nextInt(numberofrestaurants) + 1;

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        int counter = 0;
                        String restaurantName;
                        String address;
                        String rating;
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                counter++;
                                if(counter == decidedcollection) {
                                    //System.out.println("Document DATA" + document.getData().get("name").toString());
                                    restaurantName = document.getData().get("name").toString();
                                    address = document.getData().get("address").toString();
                                    rating = document.getData().get("rating").toString();
                                    restaurantnametextview.setText(restaurantName);
                                    addresstextview.setText(address);
                                    ratingtextview.setText(rating);
                                }


                            }
                        } else {

                        }
                    }
                });

    }

    /**
     * populate history DB
     * @param restaurantname
     * @param restaurantaddress
     * @param restaurantrating
     */
    private void populateHistory(String restaurantname, String restaurantaddress, String restaurantrating){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Create a new user with a first and last name
        Map<String, Object> restaurantObject = new HashMap<>();
        restaurantObject.put("name", restaurantname);
        restaurantObject.put("address", restaurantaddress);
        restaurantObject.put("rating", restaurantrating);

        db.collection("restauranthistory").add(restaurantObject);
    }


}