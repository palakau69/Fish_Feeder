package com.example.fishfeeder;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    Button button, but_atur;
    TextView textView;
    FirebaseUser user;
    private TextView gentong, voltase, jam, takaran;
    DatabaseReference databaseReference;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gentong=(TextView) findViewById(R.id.gentong);
        voltase=(TextView) findViewById(R.id.voltase);
        jam=(TextView) findViewById(R.id.jam_makan);
        takaran=(TextView)findViewById(R.id.takaran);
        textView = findViewById(R.id.user_details);
        auth = FirebaseAuth.getInstance();

        button = findViewById(R.id.button_logout);
        but_atur = findViewById(R.id.button_set);


        user = auth.getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        } else {
            textView.setText(user.getEmail());
        }

        databaseReference= FirebaseDatabase.getInstance().getReference("Feeder");
        databaseReference.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot snapshot) {
                String vgentong = snapshot.child("pakan").getValue(String.class);
                String vvolt = snapshot.child("voltase").getValue(String.class);
                String vjam = snapshot.child("ArJam").getValue(String.class);
                String vmenit = snapshot.child("ArMenit").getValue(String.class);
                String vtakaran = snapshot.child("spin").getValue(String.class);
                gentong.setText(vgentong + " %");
                voltase.setText(vvolt + " V");
                jam.setText(vjam+" : "+vmenit);
                takaran.setText(vtakaran+" x 20g");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        but_atur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("ArJam").setValue("10").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Intent intent = new Intent(getApplicationContext(), Setting.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });
    }
}

