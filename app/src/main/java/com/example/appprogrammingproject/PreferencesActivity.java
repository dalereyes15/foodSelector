package com.example.appprogrammingproject;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.prefs.Preferences;

public class PreferencesActivity extends AppCompatActivity {

    TextView setting;
    Button btnDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        setting = (TextView) findViewById(R.id.setting);
        btnDone = (Button) findViewById(R.id.done_button);

        EditText meterInput = findViewById(R.id.inputtedRadius);


        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String meterValue = meterInput.getText().toString();

                updatePreference(meterValue);

                if(meterInput.length()==0) {
                    meterInput.setError("Enter radius");
                }
                else {
                        Toast.makeText(PreferencesActivity.this, "Radius Set!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    }
                }
        });
    }

    private void updatePreference(String inputtedDistance){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference documentReference = db.collection("users").document("desireddistance");
        documentReference
                .update("radius", inputtedDistance)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }
}


