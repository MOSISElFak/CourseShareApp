package com.example.stefanzivic.courseshare;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.stefanzivic.courseshare.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    private Button signupButton,loginButton;
    private EditText emailText,passwordText,nameText,descriptionText;

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
    }

    public void createUser(String email,String password) {
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
                            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");
                            User user = new User();
                            user.setName(nameText.toString());
                            user.setInfo(descriptionText.toString());
                            user.setEmail(mAuth.getCurrentUser().getEmail());

                            Map<String, Object> userUpdates = new HashMap<String, Object>();
                            userUpdates.put(user.getEmail(), user);

                            usersRef.updateChildren(userUpdates);
                            //otvori sledecu activity

                        }

                        // ...
                    }
                });
    }

    public void openLogin() {
        Intent login = new Intent(SignupActivity.this,LoginActivity.class);
        startActivity(login);
    }
}
