package com.example.fishfeeder;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.sql.Time;
import java.util.Calendar;

public class Setting extends AppCompatActivity implements
        View.OnClickListener {

    Button btnTimePicker, btnTimePicker_2, btn_Save;
    TextView txtTime, txtTime_2, testTxt;
    int hourDAY, minuteDay;
    private int mHour, mMinute,mHour2, mMinute2;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        btnTimePicker=(Button)findViewById(R.id.btn_time);
        btnTimePicker_2=(Button)findViewById(R.id.btn_time2);
        btn_Save=(Button)findViewById(R.id.save_setting);
        txtTime=(TextView) findViewById(R.id.jam1);
        txtTime_2=(TextView) findViewById(R.id.jam2);
        testTxt = (TextView) findViewById(R.id.test);

        btnTimePicker.setOnClickListener(this);
        btnTimePicker_2.setOnClickListener(this);

        databaseReference= FirebaseDatabase.getInstance().getReference("Feeder");

        btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String arjam = String.valueOf(hourDAY);
                String armenit = String.valueOf(minuteDay);
                databaseReference.child("ArJam").setValue(arjam);
                databaseReference.child("ArMenit").setValue(armenit).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Setting.this, "success", Toast.LENGTH_SHORT).show();
                        testTxt.setText(mHour+" : "+mMinute);


                    }
                });

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
                            }else {
                                txtTime.setText(hourOfDay + " : " + minute);
                            }
                        }
                    }, mHour2, mMinute2, false);
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
                            if(minute < 10){
                                txtTime_2.setText(hourOfDay + " : 0" + minute);
                            }else {
                                txtTime_2.setText(hourOfDay + " : " + minute);
                            }
                        }
                    }, mHour2, mMinute2, false);
            timePickerDialog.show();
        }
    }
}
