package com.example.stefanzivic.courseshare;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.stefanzivic.courseshare.activities.LectureDetailsActivity;
import com.example.stefanzivic.courseshare.model.Lecture;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public static final String LECTURE_ARRAY_EXTRA = "lecturearray";

    private GoogleMap mMap;
    ArrayList<String> arrayList;
    ArrayList<MarkerOptions> places;
    List<Lecture> lectureList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        arrayList = getIntent().getStringArrayListExtra(LECTURE_ARRAY_EXTRA);
        FirebaseDatabase.getInstance().getReference("lectures").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                 lectureList  = new ArrayList<Lecture>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (arrayList.contains(snapshot.getKey())) {
                        Lecture lecture = snapshot.getValue(Lecture.class);
                        if (lecture != null) {
                            lectureList.add(lecture);
                        }
                    }
                }

                if(lectureList.size()>0) {
                    places = new ArrayList<MarkerOptions>();
                    for (Lecture lecture : lectureList
                            ) {
                        MarkerOptions marker = new MarkerOptions().position(new LatLng(lecture.getLat(),lecture.getLng())).title(lecture.getName());

                        places.add(marker);
                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MapsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        if(places.size()>0){
            for (MarkerOptions marker: places
                 ) {

                mMap.addMarker(marker).showInfoWindow();
            }
        }

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                LatLng marker = place.getLatLng();
                mMap.addMarker(new MarkerOptions().position(marker).title("Marker in Sydney"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(marker));


            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("TAG", "An error occurred: " + status);
            }
        });
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                for (Lecture lecture : lectureList
                        ) {
                    if(marker.getTitle().equals(lecture.getName())){
                        Intent intent = new Intent(MapsActivity.this, LectureDetailsActivity.class);
                        intent.putExtra(LectureDetailsActivity.LECTURE_ID_EXTRA, lecture.getId());
                        startActivity(intent);
                        return true;
                    }
                }
               return false;
            }
        });

    }

    GoogleMap.OnMapClickListener onMapClickListener  = new GoogleMap.OnMapClickListener() {
        @Override
        public void onMapClick(LatLng latLng) {
            Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                mMap.addMarker(new MarkerOptions().position(latLng).title(addresses.get(0).toString()));
                Log.d("address",addresses.get(0).toString());
            }
            catch (IOException e) {

            }

        }
    };


}
