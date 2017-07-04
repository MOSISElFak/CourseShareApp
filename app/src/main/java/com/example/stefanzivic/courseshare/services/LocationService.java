package com.example.stefanzivic.courseshare.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.stefanzivic.courseshare.NotificationReceiverActivity;
import com.example.stefanzivic.courseshare.R;
import com.example.stefanzivic.courseshare.activities.LectureDetailsActivity;
import com.example.stefanzivic.courseshare.bluetooth.BluetoothConnection;
import com.example.stefanzivic.courseshare.model.Coordinates;
import com.example.stefanzivic.courseshare.model.Lecture;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Stefan Zivic on 7/3/2017.
 */

public class LocationService extends Service {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    private class BackgroundLocationListener implements LocationListener {

        private Location mLastLocation;
        public void setLastLocation(final Location newLoc) {
            mLastLocation = newLoc;
            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null) {
                try {
                    Coordinates newLocation = new Coordinates(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                    Toast.makeText(LocationService.this,"New location: " + newLocation.toString(),Toast.LENGTH_SHORT);
                    Log.d("Mrkva",newLocation.toString());
                  //  mDatabase.child("users").child(user.getUid()).child("location")
                  //          .setValue(newLocation);
                    FirebaseDatabase.getInstance().getReference("lectures").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Lecture lecture = snapshot.getValue(Lecture.class);
                                if (lecture != null) {
                                    Location lectureLocation = new Location("");
                                    lectureLocation.setLatitude(lecture.getLat());
                                    lectureLocation.setLongitude(lecture.getLng());


                                    if (newLoc.distanceTo(lectureLocation) < 100) {
                                        Log.d("MRRRRRRRKVA", String.valueOf(newLoc.distanceTo(lectureLocation)) );
                                        Intent intent = new Intent(LocationService.this, BluetoothConnection.class);
                                        intent.putExtra(LectureDetailsActivity.LECTURE_ID_EXTRA, lecture.getId());
                                        PendingIntent pIntent = PendingIntent.getActivity(LocationService.this, (int) System.currentTimeMillis(), intent, 0);
                                        Notification noti = new Notification.Builder(LocationService.this)
                                                .setContentTitle("CourseShare-Lecture nearby")
                                                .setContentText(lecture.getName()+"is near. Click to connect with others participants").setSmallIcon(R.drawable.ic_menu_send)
                                                .setContentIntent(pIntent).build();
                                        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                        // hide the notification after its selected
                                        noti.flags |= Notification.FLAG_AUTO_CANCEL;

                                        notificationManager.notify(0, noti);

                                        stopSelf();
                                        //PendingIntent pendingIntent = PendingIntent.getService(LocationService.this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

//                                        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(LocationService.this);
//                                        notificationBuilder.setContentTitle("CourseShare Lecture nearby!");
//                                        notificationBuilder.setContentText(lecture.getName() + "is close by. Attend?");
//                                        notificationBuilder.setAutoCancel(true);
//                                        notificationBuilder.setContentIntent(pendingIntent);
//
//                                        NotificationManager notificationManager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
//                                        notificationManager.notify(0, notificationBuilder.build());

                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.e("FIREBASE_ERROR", "An error occurred while getting firebase data in background service:" + databaseError.getMessage());
                        }
                    });
                } catch (Exception e) {
                    Log.e("EXC", e.getMessage());
                }
            }
        }
        public BackgroundLocationListener(String provider) {
            setLastLocation(new Location(provider));
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.d("GPS", "Location changed");
            //Toast.makeText(LocationService.this, "Location changed", Toast.LENGTH_SHORT).show();
            setLastLocation(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            //Toast.makeText(LocationService.this, "Status changed for provider: " + provider + " " + status, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderEnabled(String provider) {
            //Toast.makeText(LocationService.this, "Provider enabled: " + provider, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderDisabled(String provider) {
            //Toast.makeText(LocationService.this, "Provider disabled: " + provider, Toast.LENGTH_SHORT).show();
        }

    }

    private LocationManager mLocationManager;
    private static final int LOCATION_INTERVAL = 30000;
    private static final float LOCATION_DISTANCE = 10f;
    LocationListener[] mLocationListeners = new LocationListener[]{
            new BackgroundLocationListener(LocationManager.GPS_PROVIDER),
            new BackgroundLocationListener(LocationManager.NETWORK_PROVIDER)
    };

    public void BackgroundLocationService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("GPS", "On start command");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Toast.makeText(getBaseContext(), "Location service started", Toast.LENGTH_SHORT).show();
        Log.e("GPS", "onCreate");
        initializeLocationManager();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            Log.e("GPS", "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.e("GPS", "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.e("GPS", "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.e("GPS", "gps provider does not exist " + ex.getMessage());
        }

        mAuth = FirebaseAuth.getInstance();
    }
    @Override
    public void onDestroy() {
        Log.e("GPS", "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i("GPS", "fail to remove location listners, ignore", ex);
                }
            }
        }
    }

    private void initializeLocationManager() {
        Log.e("GPS", "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }
}
