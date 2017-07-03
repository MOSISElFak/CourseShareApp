package com.example.stefanzivic.courseshare.activities;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.Glide;
import com.example.stefanzivic.courseshare.R;
import com.example.stefanzivic.courseshare.model.Lecture;
import com.example.stefanzivic.courseshare.model.User;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class UserDetailsActivity extends AppCompatActivity {

    public static final String USER_ID_EXTRA = "userId";
    private String userId;

    private ImageView ivPicture;
    private TextView tvName;
    private TextView tvInfo;
    private Button bViewTrainer;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_details);

        tvName = (TextView) findViewById(R.id.activity_lecture_details_name);
        tvInfo = (TextView) findViewById(R.id.activity_lecture_details_description);
        ivPicture = (ImageView) findViewById(R.id.activity_lecture_details_picture);

        userId = getIntent().getStringExtra(USER_ID_EXTRA);

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
                        StorageReference ref = storage.getReference().child(user.getPicture());

                        Glide.with(UserDetailsActivity.this).using(new FirebaseImageLoader()).load(ref).into(ivPicture);
                    }
                    else {
                        ivPicture.setImageResource(0);
                        TextDrawable drawable = TextDrawable.builder()
                                .buildRoundRect(tvName.getText().toString().substring(0, 1), Color.DKGRAY, 16);
                        ivPicture.setImageDrawable(drawable);
                    }
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
    }
}
