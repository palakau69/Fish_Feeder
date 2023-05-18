package com.example.fishfeeder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private CardView D1, D2, D3, D4, D5;
    private Button button;
    private TextView textView;
    private FirebaseUser user;
    private FirebaseAuth auth;
    private TextView gentong, voltase, jam, jam2, takaran;
    private DatabaseReference databaseReference;

    private NotificationManagerCompat notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        D1 = findViewById(R.id.d1);
        D2 = findViewById(R.id.d2);
        D3 = findViewById(R.id.d3);
        D4 = findViewById(R.id.d4);
        D5 = findViewById(R.id.d5);

        D1.setOnClickListener(this);
        D2.setOnClickListener(this);
        D3.setOnClickListener(this);
        D4.setOnClickListener(this);
        D5.setOnClickListener(this);

        gentong = findViewById(R.id.gentong);
        voltase = findViewById(R.id.voltase);
        jam = findViewById(R.id.jam_makan);
        jam2 = findViewById(R.id.jam_makan_2);
        takaran = findViewById(R.id.takaran);
        textView = findViewById(R.id.user_details);
        button = findViewById(R.id.button_logout);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        } else {
            textView.setText(user.getEmail());
        }

        notificationManager = NotificationManagerCompat.from(this);
        NotificationHelper notificationHelper = new NotificationHelper(this);
        notificationHelper.requestNotificationPermission(this);

        databaseReference = FirebaseDatabase.getInstance().getReference("Feeder");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
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

                if (numgentong <= 30) {
                    showNotification("Pakan Mau Habis!", "Sepertinya pakan kamu mau habis, segera isi ulang!", 1);
                }
                if (numvolt <= 10.0) {
                    showNotification("Batre Lemah", "Isi daya perangkat segera!", 2);
                }
                if (vstatus.equals("on")) {
                    showNotification("Memberi makan ikan", "Perangkat hidup untuk memberi makan ikan!", 3);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLogoutConfirmationDialog();
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
            case R.id.d2:
                i = new Intent(this, D2.class);
                startActivity(i);
                break;
            case R.id.d3:
                i = new Intent(this, D3.class);
                startActivity(i);
                break;
            case R.id.d4:
                i = new Intent(this, D4.class);
                startActivity(i);
                break;
            case R.id.d5:
                i = new Intent(this, D5.class);
                startActivity(i);
                break;
        }
    }

    private void showNotification(String title, String message, int notificationId) {
        String CHANNEL_ID = "your_channel_id";
        String CHANNEL_NAME = "Your Channel";
        String CHANNEL_DESCRIPTION = "Your Channel Description";

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(CHANNEL_DESCRIPTION);
            notificationManager.createNotificationChannel(channel);
        }

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.icon_app)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_CALL)
                .setFullScreenIntent(pendingIntent, true)
                .setAutoCancel(true);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(notificationId, builder.build());
    }

    // Method to show the logout confirmation dialog
    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Dismiss the dialog
                dialog.dismiss();
            }
        });

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
