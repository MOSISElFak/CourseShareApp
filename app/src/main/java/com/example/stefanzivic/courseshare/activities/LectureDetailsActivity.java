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
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class LectureDetailsActivity extends AppCompatActivity {

    public static final String LECTURE_ID_EXTRA = "lectureId";
    private String lectureId;

    private ImageView ivPicture;
    private TextView tvName;
    private TextView tvDescription;
    private Button bViewTrainer;

    private Lecture lecture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_details);

        tvName = (TextView) findViewById(R.id.activity_lecture_details_name);
        tvDescription = (TextView) findViewById(R.id.activity_lecture_details_description);
        ivPicture = (ImageView) findViewById(R.id.activity_lecture_details_picture);

        lectureId = getIntent().getStringExtra(LECTURE_ID_EXTRA);

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

                    if (lecture.getPicture() != null) {
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference ref = storage.getReference().child(lecture.getPicture());

                        Glide.with(LectureDetailsActivity.this).using(new FirebaseImageLoader()).load(ref).into(ivPicture);
                    }
                    else {
                        ivPicture.setImageResource(0);
                        TextDrawable drawable = TextDrawable.builder()
                                .buildRoundRect(tvName.getText().toString().substring(0, 1), Color.DKGRAY, 16);
                        ivPicture.setImageDrawable(drawable);
                    }
                }
                else {
                    tvName.setText("Selected lecture doesn't exist anymore");
                    tvDescription.setText("");
                    ivPicture.setImageResource(0);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(LectureDetailsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
