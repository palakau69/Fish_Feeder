package com.example.fishfeeder;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
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
    private TextView gentong, voltase, jam, jam2, takaran;
    DatabaseReference databaseReference;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        D1 = (CardView) findViewById(R.id.d1);
//        D2= (CardView) findViewById(R.id.d2);
//        D3= (CardView) findViewById(R.id.d3);
        D4 = (CardView) findViewById(R.id.d4);
//        D5= (CardView) findViewById(R.id.d5);
//        D6= (CardView) findViewById(R.id.d6);

        D1.setOnClickListener((View.OnClickListener) this);
//        D2.setOnClickListener((View.OnClickListener) this);
//        D3.setOnClickListener((View.OnClickListener) this);
        D4.setOnClickListener((View.OnClickListener) this);
//        D5.setOnClickListener((View.OnClickListener) this);
//        D6.setOnClickListener((View.OnClickListener) this);

        gentong = (TextView) findViewById(R.id.gentong);
        voltase = (TextView) findViewById(R.id.voltase);
        jam = (TextView) findViewById(R.id.jam_makan);
        jam2 = (TextView) findViewById(R.id.jam_makan_2);
        takaran = (TextView) findViewById(R.id.takaran);
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

        databaseReference = FirebaseDatabase.getInstance().getReference("Feeder");
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
                String vstatus = snapshot.child("status").getValue(String.class);
                gentong.setText(vgentong + " %");
                voltase.setText(vvolt + " V");
                jam.setText(vjam + ":" + vmenit);
                jam2.setText(vjam2 + ":" + vmenit2);
                takaran.setText(vtakaran + " x 20g");

                int numgentong = Integer.parseInt(vgentong);
                float numvolt = Float.parseFloat(vvolt);

                if (numgentong <= 30){
                    NotificationGentong();
                }
                if (numvolt <= 10.0){
                    NotificationVolt();
                }
                if (vstatus.equals("on")){
                    NotificationStatus();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        but_atur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationGentong();
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
        Intent i;
        switch (view.getId()) {
            case R.id.d1:
                i = new Intent(this, D1.class);
                startActivity(i);
                break;
            case R.id.d4:
                i = new Intent(this, D4.class);
                startActivity(i);
                break;
        }
    }


    private void NotificationGentong() {
        // Create a notification channel
        String CHANNEL_ID = "0";
        String CHANNEL_NAME = "Notifikasi Pakan";
        String CHANNEL_DESCRIPTION = "Notifikasi Pakan";

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription(CHANNEL_DESCRIPTION);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        int NOTIFICATION_ID = 1;
        String title = "Pakan Mau Habis!";
        String message = "Sepertinya pakan kamu mau habis, segera isi ulang!";
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.icon_app)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private void NotificationVolt() {
        // Create a notification channel
        String CHANNEL_ID = "1";
        String CHANNEL_NAME = "Notifikasi Daya";
        String CHANNEL_DESCRIPTION = "Notifikasi Daya";

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription(CHANNEL_DESCRIPTION);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        int NOTIFICATION_ID = 2;
        String title = "Batre Lemah";
        String message = "Isi daya perangkat segera!";
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.icon_app)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private void NotificationStatus() {
        // Create a notification channel
        String CHANNEL_ID = "2";
        String CHANNEL_NAME = "Notifikasi Alarm";
        String CHANNEL_DESCRIPTION = "Notifikasi Alarm";

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription(CHANNEL_DESCRIPTION);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        int NOTIFICATION_ID = 3;
        String title = "Memberi makan ikan";
        String message = "Perangkat hidup untuk memberi makan ikan!";
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.icon_app)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

}

