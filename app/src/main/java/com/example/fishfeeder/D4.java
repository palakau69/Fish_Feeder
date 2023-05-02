package com.example.fishfeeder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class D4 extends AppCompatActivity {

    TextInputEditText takaran;
    Button save;
    String strtakaran;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d4);

        takaran = (TextInputEditText) findViewById(R.id.Et_takar);
        save = (Button) findViewById(R.id.save_setting);
        databaseReference = FirebaseDatabase.getInstance().getReference("Feeder");
        databaseReference.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot snapshot) {
                String vtakaran = snapshot.child("spin").getValue(String.class);
                takaran.setText(vtakaran);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }

        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strtakaran = String.valueOf(takaran.getText());
                databaseReference.child("spin").setValue(strtakaran).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(D4.this, "success", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });
    }
}