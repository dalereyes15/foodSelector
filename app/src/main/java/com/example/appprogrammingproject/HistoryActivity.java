package com.example.appprogrammingproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    JSONArray data = new JSONArray();

    FirebaseFirestore db;
    ArrayList<Restaurant> previous_restaurants = new ArrayList<Restaurant>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.previous);

        db =FirebaseFirestore.getInstance();

        db.collection("restaurants")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for(QueryDocumentSnapshot doc : task.getResult()) {
                        Restaurant restaurant = doc.toObject(Restaurant.class);
                        previous_restaurants.add(restaurant);
                        RecyclerView rv = findViewById(R.id.recycler);
                        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
                        RecyclerView.Adapter adapter = new RestaurantListAdapter(previous_restaurants);
                        rv.setHasFixedSize(true);
                        rv.setLayoutManager(llm);
                        rv.setAdapter(adapter);
                    }
                }
            }
        });





    }

    class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.ViewHolder> {
        ArrayList<Restaurant> data;
        class ViewHolder extends RecyclerView.ViewHolder {
            TextView name;
            TextView rating;
            ViewHolder(View v) {
                super(v);
                name = v.findViewById(R.id.restaurant_name);
                rating = v.findViewById(R.id.restaurant_rating);
            }
        }

        RestaurantListAdapter(ArrayList<Restaurant> data) {
            for(Restaurant restaurant : data) {
                if(restaurant.getRating().length() == 1) {
                    String rating = restaurant.getRating();
                    restaurant.setRating(rating+=".0");
                }
            }
            this.data = data;
        }

        @Override
        public RestaurantListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item, parent, false);
            ViewHolder vh = new ViewHolder(view);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull RestaurantListAdapter.ViewHolder holder, int position) {
            holder.name.setText(data.get(position).getName());
            holder.rating.setText(data.get(position).getRating());
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

}
