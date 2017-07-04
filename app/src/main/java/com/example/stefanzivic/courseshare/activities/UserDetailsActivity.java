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
import com.example.stefanzivic.courseshare.MainActivity;
import com.example.stefanzivic.courseshare.R;
import com.example.stefanzivic.courseshare.bluetooth.BluetoothConnection;
import com.example.stefanzivic.courseshare.model.Lecture;
import com.example.stefanzivic.courseshare.model.User;
//import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Map;

public class UserDetailsActivity extends AppCompatActivity {

    public static final String USER_ID_EXTRA = "userId";
    private String userId;

    private ImageView ivPicture;
    private TextView tvName;
    private TextView tvInfo;

    private Button bFutureLectures;
    private Button bPastLectures;
    private Button bAttendedLectures;
    private Button bFollow;
    private boolean following;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        tvName = (TextView) findViewById(R.id.activity_user_details_name);
        tvInfo = (TextView) findViewById(R.id.activity_user_details_description);
        ivPicture = (ImageView) findViewById(R.id.activity_user_details_picture);

        bFutureLectures = (Button) findViewById(R.id.activity_user_details_future_lectures_button);
        bPastLectures = (Button) findViewById(R.id.activity_user_details_past_lectures_button);
        bAttendedLectures = (Button) findViewById(R.id.activity_user_details_attended_lectures_button);
        bFollow = (Button) findViewById(R.id.activity_user_details_follow_button);
        following = false;
        Button bluetooth = (Button)findViewById(R.id.bluetooth);
        bluetooth.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(UserDetailsActivity.this, BluetoothConnection.class);
                        startActivity(intent);
                    }
                }
        );

        userId = getIntent().getStringExtra(USER_ID_EXTRA);

        bFutureLectures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserDetailsActivity.this, MainActivity.class);
                intent.putExtra(MainActivity.TYPE_EXTRA, 1);
                intent.putExtra(MainActivity.USER_ID_EXTRA, user.getId());
                startActivity(intent);
            }
        });

        bPastLectures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserDetailsActivity.this, MainActivity.class);
                intent.putExtra(MainActivity.TYPE_EXTRA, 2);
                intent.putExtra(MainActivity.USER_ID_EXTRA, user.getId());
                startActivity(intent);
            }
        });

        bAttendedLectures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UserDetailsActivity.this, "Ovo treba uraditi", Toast.LENGTH_SHORT).show();
            }
        });


        FirebaseDatabase.getInstance().getReference("users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);

                if (user != null) {
                    if (user.getName() != null) {
                        tvName.setText(user.getName());
                    }
                    else {
                        tvName.setText("<Lecture has no name>");
                    }

                    if (user.getInfo() != null) {
                        tvInfo.setText(user.getInfo());
                    }
                    else {
                        tvInfo.setText("<Lecture has no description");
                    }

                    if (user.getPicture() != null) {
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference ref = storage.getReference().child("images").child(user.getPicture());

                        Glide.with(UserDetailsActivity.this).using(new FirebaseImageLoader()).load(ref).into(ivPicture);
                        //Picasso.with(UserDetailsActivity.this).load(user.getPicture()).into(ivPicture);
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
                                if (currentUser.get_favouriteTeachers() == null || !currentUser.get_favouriteTeachers().containsKey(userId)) {
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
                    tvName.setText("Selected user doesn't exist anymore");
                    tvInfo.setText("");
                    ivPicture.setImageResource(0);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(UserDetailsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

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
                                Map<String, Object> pom = currentUser.get_favouriteTeachers();
                                pom.remove(userId);
                                FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("_favouriteTeachers").setValue(pom);
                                bFollow.setText("Follow");
                                following = false;
                            }
                            else {
                                //FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("_interestedLectures").setValue(lectureId, "true");
                                Map<String, Object> pom = currentUser.get_favouriteTeachers();
                                pom.put(userId, true);
                                FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("_favouriteTeachers").setValue(pom);
                                bFollow.setText("Unfollow");
                                following = true;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(UserDetailsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
