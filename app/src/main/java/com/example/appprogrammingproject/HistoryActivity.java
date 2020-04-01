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

import org.json.JSONArray;

public class HistoryActivity extends AppCompatActivity {
    
    JSONArray data = new JSONArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.previous);

        data.put("Champions");
        data.put("Lin's Garden");
        data.put("He");
        data.put("The Hop Yard");
        data.put("Carl's Jr.");
        data.put("Middletown Pizza");

        RecyclerView rv = findViewById(R.id.recycler);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        RecyclerView.Adapter adapter = new RestaurantListAdapter(data);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(llm);
        rv.setAdapter(adapter);
    }

    class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.ViewHolder> {
        JSONArray data;
        class ViewHolder extends RecyclerView.ViewHolder {
            TextView name;
            ViewHolder(View v) {
                super(v);
                name = v.findViewById(R.id.rest_name);
            }
        }

        RestaurantListAdapter(JSONArray data) {
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
            holder.name.setText("HELLO TESTING");
        }

        @Override
        public int getItemCount() {
            return data.length();
        }
    }

}
