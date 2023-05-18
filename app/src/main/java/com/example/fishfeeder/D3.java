package com.example.fishfeeder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class D3 extends AppCompatActivity {

    TextView gentong;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d3);

        gentong = (TextView) findViewById(R.id.penyimpanan);

        databaseReference = FirebaseDatabase.getInstance().getReference("Feeder");
        databaseReference.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot snapshot) {
                String vgentong = snapshot.child("pakan").getValue(String.class);
                gentong.setText(vgentong + " %");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
