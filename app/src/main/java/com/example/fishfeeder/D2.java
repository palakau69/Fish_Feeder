package com.example.fishfeeder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class D2 extends AppCompatActivity {

    TextView voltase;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d2);

        voltase = (TextView) findViewById(R.id.inVol);

        databaseReference = FirebaseDatabase.getInstance().getReference("Feeder");
        databaseReference.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot snapshot) {
                String vvolt = snapshot.child("voltase").getValue(String.class);
                voltase.setText(vvolt + " V");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}