package com.example.stefanzivic.courseshare.activities;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.Glide;
import com.example.stefanzivic.courseshare.R;
import com.example.stefanzivic.courseshare.model.User;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    EditText etName;
    EditText etInfo;
    ImageView ivPicture;
    Button bPicture;
    Button bApply;
    Button bCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        etName = (EditText) findViewById(R.id.activity_edit_profile_name);
        etInfo = (EditText) findViewById(R.id.activity_edit_profile_info);
        ivPicture = (ImageView) findViewById(R.id.activity_edit_profile_picture);
        bPicture = (Button) findViewById(R.id.activity_edit_profile_picture_button);
        bApply = (Button) findViewById(R.id.activity_edit_profile_apply_button);
        bCancel = (Button) findViewById(R.id.activity_edit_profile_cancel_button);


        bPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        bApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> updateMap = new HashMap<>();
                updateMap.put("name", etName.getText().toString());
                updateMap.put("info", etInfo.getText().toString());

                String key = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                FirebaseDatabase.getInstance().getReference("users").child(key).updateChildren(updateMap);
            }
        });

        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadUser();
            }
        });

        loadUser();
    }


    public void loadUser() {
        String key = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        FirebaseDatabase.getInstance().getReference("users").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                if (user != null) {
                    if (user.getName() != null) {
                        etName.setText(user.getName());
                    }
                    else {
                        etName.setText("<Lecture has no name>");
                    }

                    if (user.getInfo() != null) {
                        etInfo.setText(user.getInfo());
                    }
                    else {
                        etInfo.setText("<Lecture has no description");
                    }

                    if (user.getPicture() != null) {
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference ref = storage.getReference().child(user.getPicture());

                        Glide.with(EditProfileActivity.this).using(new FirebaseImageLoader()).load(ref).into(ivPicture);
                        //Picasso.with(EditProfileActivity.this).load(user.getPicture()).into(ivPicture);
                    }
                    else {
                        ivPicture.setImageResource(0);
                        TextDrawable drawable = TextDrawable.builder()
                                .buildRoundRect(etName.getText().toString().substring(0, 1), Color.DKGRAY, 16);
                        ivPicture.setImageDrawable(drawable);
                    }
                }
                else {
                    etName.setText("Selected user doesn't exist anymore");
                    etInfo.setText("");
                    ivPicture.setImageResource(0);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(EditProfileActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
