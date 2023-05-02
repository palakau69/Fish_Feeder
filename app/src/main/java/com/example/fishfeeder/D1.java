package com.example.fishfeeder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class D1 extends AppCompatActivity implements View.OnClickListener {

    Button btnTimePicker, btnTimePicker_2, btn_Save;
    TextView txtTime, txtTime_2, testTxt;
    int hourDAY, minuteDay, hourDAY_2, minuteDay_2;
    String arjam, armenit, arjam_2, armenit_2;
    private int mHour, mMinute,mHour2, mMinute2;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d1);

        btnTimePicker=(Button)findViewById(R.id.btn_time);
        btnTimePicker_2=(Button)findViewById(R.id.btn_time2);
        btn_Save=(Button)findViewById(R.id.save_setting);
        txtTime=(TextView) findViewById(R.id.jam1);
        txtTime_2=(TextView) findViewById(R.id.jam2);

        btnTimePicker.setOnClickListener(this);
        btnTimePicker_2.setOnClickListener(this);

        databaseReference= FirebaseDatabase.getInstance().getReference("Feeder");
        databaseReference.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot snapshot) {
                String vjam = snapshot.child("ArJam").getValue(String.class);
                String vmenit = snapshot.child("ArMenit").getValue(String.class);
                String vjam_2 = snapshot.child("ArJam2").getValue(String.class);
                String vmenit_2 = snapshot.child("ArMenit2").getValue(String.class);
                //String vtakaran = snapshot.child("spin").getValue(String.class);

                txtTime.setText(vjam+" : "+vmenit);
                txtTime_2.setText(vjam_2+" : "+vmenit_2);
                //takaran.setText(vtakaran+" x 20g");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (armenit != null) {
                    //SET AlRM 1
                    arjam = String.valueOf(hourDAY);
                    databaseReference.child("ArJam").setValue(arjam);
                    databaseReference.child("ArMenit").setValue(armenit).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(D1.this, "success", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });

                }if (armenit_2 !=null){
                    //SET AlRM 2
                    arjam_2= String.valueOf(hourDAY_2);
                    databaseReference.child("ArJam2").setValue(arjam_2);
                    databaseReference.child("ArMenit2").setValue(armenit_2);
                }else{
                    Toast.makeText(D1.this, "Menyimpan Perubahan!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onClick(View v) {

        if (v == btnTimePicker) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            hourDAY = hourOfDay;
                            minuteDay = minute;
                            if(minute < 10){
                                txtTime.setText(hourOfDay + " : 0" + minute);
                                armenit = String.valueOf("0"+minuteDay);
                            }else {
                                txtTime.setText(hourOfDay + " : " + minute);
                                armenit = String.valueOf(minuteDay);
                            }
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }

        if (v == btnTimePicker_2) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour2 = c.get(Calendar.HOUR_OF_DAY);
            mMinute2 = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            hourDAY_2 = hourOfDay;
                            minuteDay_2 = minute;
                            if(minute < 10){
                                txtTime_2.setText(hourOfDay + " : 0" + minute);
                                armenit_2 = String.valueOf("0"+minuteDay_2);
                            }else {
                                txtTime_2.setText(hourOfDay + " : " + minute);
                                armenit_2 = String.valueOf(minuteDay_2);
                            }
                        }
                    }, mHour2, mMinute2, false);
            timePickerDialog.show();
        }
    }
}