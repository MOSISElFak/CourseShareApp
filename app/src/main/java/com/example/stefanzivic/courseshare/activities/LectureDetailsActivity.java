package com.example.stefanzivic.courseshare.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
//import com.bumptech.glide.Glide;
import com.bumptech.glide.Glide;
import com.example.stefanzivic.courseshare.MapsActivity;
import com.example.stefanzivic.courseshare.R;
import com.example.stefanzivic.courseshare.model.Lecture;
//import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.example.stefanzivic.courseshare.model.User;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Map;

public class LectureDetailsActivity extends AppCompatActivity {

    public static final String LECTURE_ID_EXTRA = "lectureId";
    private String lectureId;

    private ImageView ivPicture;
    private TextView tvName;
    private TextView tvDescription;
    private Button bViewTrainer;
    private Button bShowOnMap;
    private Button bFollow;
    private Boolean following;
    private Button bUser;

    private TextView tvAddress;
    private TextView tvDate;

    private Lecture lecture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_details);

        tvName = (TextView) findViewById(R.id.activity_lecture_details_name);
        tvDescription = (TextView) findViewById(R.id.activity_lecture_details_description);
        ivPicture = (ImageView) findViewById(R.id.activity_lecture_details_picture);
        bShowOnMap = (Button)findViewById(R.id.activity_lecture_details_show_on_map_button) ;
        lectureId = getIntent().getStringExtra(LECTURE_ID_EXTRA);
        bShowOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLectureOnMap();
            }
        });
        bFollow = (Button) findViewById(R.id.activity_lecture_details_follow_button);
        bUser = (Button) findViewById(R.id.activity_lecture_details_user_button);

        tvAddress = (TextView) findViewById(R.id.activity_lecture_details_address);
        tvDate = (TextView) findViewById(R.id.activity_lecture_details_date) ;

        FirebaseDatabase.getInstance().getReference("lectures").child(lectureId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lecture = dataSnapshot.getValue(Lecture.class);

                if (lecture != null) {
                    if (lecture.getName() != null) {
                        tvName.setText(lecture.getName());
                    }
                    else {
                        tvName.setText("<Lecture has no name>");
                    }

                    if (lecture.getDescription() != null) {
                        tvDescription.setText(lecture.getDescription());
                    }
                    else {
                        tvDescription.setText("<Lecture has no description");
                    }

                    if (lecture.getAddress() != null) {
                        tvAddress.setText(lecture.getAddress());
                    }

                    tvDate.setText(lecture.getDay() + "." + lecture.getMonth() + "." + lecture.getYear() + " ; " + lecture.getHour() + ":" + lecture.getMinute());

                    if (lecture.getPicture() != null) {
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference ref = storage.getReference().child("images").child(lecture.getPicture());

                        //LectureDetailsActivity.this
                        Glide.with(getBaseContext()).using(new FirebaseImageLoader()).load(ref).into(ivPicture);
                        //Picasso.with(LectureDetailsActivity.this).load(lecture.getPicture()).into(ivPicture);
                    }
                    else {
                        ivPicture.setImageResource(0);
                        TextDrawable drawable = TextDrawable.builder()
                                .buildRoundRect(tvName.getText().toString().substring(0, 1), Color.DKGRAY, 16);
                        ivPicture.setImageDrawable(drawable);
                    }

                    FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            bFollow.setVisibility(View.VISIBLE);
                            User currentUser = dataSnapshot.getValue(User.class);
                            if (currentUser != null) {
                                if (currentUser.get_interestedLectures() == null || !currentUser.get_interestedLectures().containsKey(lectureId)) {
                                    bFollow.setText("Follow");
                                    following = false;
                                }
                                else {
                                    bFollow.setText("Unfollow");
                                    following = true;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            bFollow.setVisibility(View.INVISIBLE);
                        }
                    });
                }
                else {
                    tvName.setText("Selected lecture doesn't exist anymore");
                    tvDescription.setText("");
                    ivPicture.setImageResource(0);
                }

                bFollow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                User currentUser = dataSnapshot.getValue(User.class);
                                if (currentUser != null) {
                                    if (following) {
                                        //User currentUser = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("_interestedLectures").setValue(lectureId, null);
                                        Map<String, Object> pom = currentUser.get_interestedLectures();
                                        pom.remove(lectureId);
                                        FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("_interestedLectures").setValue(pom);
                                        bFollow.setText("Follow");
                                        following = false;
                                    }
                                    else {
                                        //FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("_interestedLectures").setValue(lectureId, "true");
                                        Map<String, Object> pom = currentUser.get_interestedLectures();
                                        pom.put(lectureId, true);
                                        FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("_interestedLectures").setValue(pom);
                                        bFollow.setText("Unfollow");
                                        following = true;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(LectureDetailsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(LectureDetailsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        bUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LectureDetailsActivity.this, UserDetailsActivity.class);
                intent.putExtra(UserDetailsActivity.USER_ID_EXTRA, lecture.get_user());
                startActivity(intent);
            }
        });
    }

    public void showLectureOnMap() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(lectureId);
        Intent intent = new Intent(LectureDetailsActivity.this, MapsActivity.class);
        intent.putExtra(MapsActivity.LECTURE_ARRAY_EXTRA, arrayList);
        startActivity(intent);
    }
}
