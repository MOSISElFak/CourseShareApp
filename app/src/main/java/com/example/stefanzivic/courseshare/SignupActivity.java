package com.example.stefanzivic.courseshare;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.stefanzivic.courseshare.activities.CreateLectureActivity;
import com.example.stefanzivic.courseshare.model.User;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    private Button signupButton,loginButton;
    private EditText emailText,passwordText,nameText,descriptionText;

    private ImageView ivPicture;
    private Button bPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        emailText = (EditText)findViewById(R.id.text_email);
        passwordText = (EditText)findViewById(R.id.text_password);
        nameText = (EditText)findViewById(R.id.text_name);
        descriptionText = (EditText)findViewById(R.id.text_description);
        signupButton = (Button)findViewById(R.id.signup_button);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser(emailText.getText().toString(),passwordText.getText().toString());
            }
        });
        loginButton = (Button)findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLogin();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (user != null) {
                    // User is signed in
                    Log.d("TAG", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("TAG", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        ivPicture = (ImageView) findViewById(R.id.activity_signup_picture);
        bPicture = (Button) findViewById(R.id.activity_signup_picture_button);

        bPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });
    }

    public void createUser(String email,String password) {
        Log.d(email,password);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("TAG", "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(SignupActivity.this, "Auth failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else {
                            final DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");
                            User user = new User();
                            user.setId(mAuth.getCurrentUser().getUid());
                            user.setName(nameText.toString());
                            user.setInfo(descriptionText.toString());
                            user.setEmail(mAuth.getCurrentUser().getEmail());


                            if (ivPicture.getDrawable() != null) {
                                user.setPicture(mAuth.getCurrentUser().getUid());

                                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images").child(user.getId());
                                ivPicture.setDrawingCacheEnabled(true);
                                ivPicture.buildDrawingCache();
                                Bitmap bitmap = ivPicture.getDrawingCache();
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                byte[] data = baos.toByteArray();

                                UploadTask uploadTask = storageReference.putBytes(data);
                                uploadTask.addOnFailureListener(SignupActivity.this, new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(SignupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }

                            Map<String, Object> userUpdates = new HashMap<String, Object>();
                            userUpdates.put(user.getId(), user);

                            usersRef.updateChildren(userUpdates);
                            //otvori sledecu activity


                            Intent intent = new Intent(SignupActivity.this,MainActivity.class);
                            startActivity(intent);

                        }

                        // ...
                    }
                });
    }

    public void openLogin() {
        Intent login = new Intent(SignupActivity.this,LoginActivity.class);
        startActivity(login);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                return;
            }
            try {
                InputStream inputStream = SignupActivity.this.getContentResolver().openInputStream(data.getData());
                ivPicture.setImageBitmap(BitmapFactory.decodeStream(inputStream));

            } catch (FileNotFoundException e) {
                Toast.makeText(SignupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
