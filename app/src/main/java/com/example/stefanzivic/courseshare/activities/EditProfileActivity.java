package com.example.stefanzivic.courseshare.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.Glide;
import com.example.stefanzivic.courseshare.MainActivity;
import com.example.stefanzivic.courseshare.R;
import com.example.stefanzivic.courseshare.SignupActivity;
import com.example.stefanzivic.courseshare.model.User;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    private boolean pictureModified;

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

        pictureModified = false;

        etName = (EditText) findViewById(R.id.activity_edit_profile_name);
        etInfo = (EditText) findViewById(R.id.activity_edit_profile_info);
        ivPicture = (ImageView) findViewById(R.id.activity_edit_profile_picture);
        bPicture = (Button) findViewById(R.id.activity_edit_profile_picture_button);
        bApply = (Button) findViewById(R.id.activity_edit_profile_apply_button);
        bCancel = (Button) findViewById(R.id.activity_edit_profile_cancel_button);


        bPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });

        bApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> updateMap = new HashMap<>();
                updateMap.put("name", etName.getText().toString());
                updateMap.put("info", etInfo.getText().toString());

                String key = FirebaseAuth.getInstance().getCurrentUser().getUid();
                FirebaseDatabase.getInstance().getReference("users").child(key).updateChildren(updateMap);

                if (pictureModified) {
                    FirebaseDatabase.getInstance().getReference("users").child(key).child("picture").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());

                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    ivPicture.setDrawingCacheEnabled(true);
                    ivPicture.buildDrawingCache();
                    Bitmap bitmap = ivPicture.getDrawingCache();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data = baos.toByteArray();

                    UploadTask uploadTask = storageReference.putBytes(data);
                    uploadTask.addOnFailureListener(EditProfileActivity.this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
                }

                Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
                startActivity(intent);
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
        String key = FirebaseAuth.getInstance().getCurrentUser().getUid();
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                return;
            }
            try {
                InputStream inputStream = EditProfileActivity.this.getContentResolver().openInputStream(data.getData());
                ivPicture.setImageBitmap(BitmapFactory.decodeStream(inputStream));
                pictureModified = true;

            } catch (FileNotFoundException e) {
                Toast.makeText(EditProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static final int PICK_IMAGE = 111;
    public void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE);
    }
}
