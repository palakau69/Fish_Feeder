package com.example.fishfeeder;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private CardView D1, D4;
    Button button, but_atur;
    TextView textView;
    FirebaseUser user;
    FirebaseAuth auth;
    private TextView gentong, voltase, jam,jam2, takaran;
    DatabaseReference databaseReference;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        D1= (CardView) findViewById(R.id.d1);
//        D2= (CardView) findViewById(R.id.d2);
//        D3= (CardView) findViewById(R.id.d3);
        D4= (CardView) findViewById(R.id.d4);
//        D5= (CardView) findViewById(R.id.d5);
//        D6= (CardView) findViewById(R.id.d6);

        D1.setOnClickListener((View.OnClickListener) this);
//        D2.setOnClickListener((View.OnClickListener) this);
//        D3.setOnClickListener((View.OnClickListener) this);
        D4.setOnClickListener((View.OnClickListener) this);
//        D5.setOnClickListener((View.OnClickListener) this);
//        D6.setOnClickListener((View.OnClickListener) this);

        gentong=(TextView) findViewById(R.id.gentong);
        voltase=(TextView) findViewById(R.id.voltase);
        jam=(TextView) findViewById(R.id.jam_makan);
        jam2=(TextView) findViewById(R.id.jam_makan_2);
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
                String vjam2 = snapshot.child("ArJam2").getValue(String.class);
                String vmenit2 = snapshot.child("ArMenit2").getValue(String.class);
                String vtakaran = snapshot.child("spin").getValue(String.class);
                gentong.setText(vgentong + " %");
                voltase.setText(vvolt + " V");
                jam.setText(vjam+":"+vmenit);
                jam2.setText(vjam2+":"+vmenit2);
                takaran.setText(vtakaran+" x 20g");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        but_atur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Setting.class);
                startActivity(intent);
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

    @Override
    public void onClick(View view) {
        Intent i ;
        switch (view.getId()){
            case R.id.d1: i = new Intent(this,D1.class); startActivity(i); break;
            case R.id.d4: i = new Intent(this,D4.class); startActivity(i); break;
        }
    }
}

