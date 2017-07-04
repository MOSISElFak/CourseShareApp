package com.example.stefanzivic.courseshare.activities;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.stefanzivic.courseshare.MainActivity;
import com.example.stefanzivic.courseshare.MapsPickerActivity;
import com.example.stefanzivic.courseshare.R;
import com.example.stefanzivic.courseshare.SignupActivity;
import com.example.stefanzivic.courseshare.model.Lecture;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerController;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;

public class CreateLectureActivity extends AppCompatActivity {

    private final int MAPS_PICKER_ACTIVITY_RESULT = 1;
    private boolean placeSet;
    private boolean dateSet;
    private boolean timeSet;

    ImageView ivPicture;

    EditText etName;
    EditText etDescription;

    Button bPicture;
    Button bCreate;
    Button bCancel;

    Lecture lecture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_lecture);

        lecture = new Lecture();

        placeSet = false;
        dateSet = false;
        timeSet = false;

        ivPicture = (ImageView) findViewById(R.id.activity_create_lecture_picture);
        etName = (EditText) findViewById(R.id.activity_create_lecture_name);
        etDescription = (EditText) findViewById(R.id.activity_create_lecture_description);

        bPicture = (Button) findViewById(R.id.activity_create_lecture_picture_button);
        bCreate = (Button) findViewById(R.id.activity_create_lecture_create_button);
        bCancel = (Button) findViewById(R.id.activity_create_lecture_cancel_button);

        bCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etName.getText().toString().isEmpty()) {

                    lecture.setName(etName.getText().toString());
                    if (!etDescription.getText().toString().isEmpty()) {
                        lecture.setDescription(etDescription.getText().toString());
                    }
                    lecture.set_user(FirebaseAuth.getInstance().getCurrentUser().getUid());

                    if (!placeSet) {
                        showPlacePicker();
                    }
                    else if (!dateSet) {
                        showDatePicker();
                    }
                    else {
                        showTimePicker();
                    }
                }
                else {
                    Toast.makeText(CreateLectureActivity.this, "Name your lecture.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateLectureActivity.this.finish();
            }
        });

        bPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });
    }

    public void showPlacePicker() {
        Intent intent = new Intent(CreateLectureActivity.this, MapsPickerActivity.class);
        startActivityForResult(intent,MAPS_PICKER_ACTIVITY_RESULT);
    }

    public void showDatePicker() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                lecture.setYear(year);
                lecture.setMonth(monthOfYear + 1); //vraca od 0 do 11
                lecture.setDay(dayOfMonth);

                dateSet = true;
                showTimePicker();
            }
        }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show(getFragmentManager(), "tag");
    }

    public void showTimePicker() {
        Calendar now = Calendar.getInstance();
        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                lecture.setHour(hourOfDay);
                lecture.setMinute(minute);

                //lecture.setRunning(false);
                lecture.setPast(false);

                timeSet = true;

                FirebaseDatabase.getInstance().getReference("lectures").push().setValue(lecture, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseReference != null) {

                            databaseReference.child("id").setValue(databaseReference.getKey());
                            databaseReference.child("picture").setValue(databaseReference.getKey());

                            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images").child(databaseReference.getKey());
                            ivPicture.setDrawingCacheEnabled(true);
                            ivPicture.buildDrawingCache();
                            Bitmap bitmap = ivPicture.getDrawingCache();
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            byte[] data = baos.toByteArray();

                            UploadTask uploadTask = storageReference.putBytes(data);
                            uploadTask.addOnFailureListener(CreateLectureActivity.this, new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(CreateLectureActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            });

                            Intent intent = new Intent(CreateLectureActivity.this, MainActivity.class);
                            Toast.makeText(CreateLectureActivity.this, "Lecture successfully created", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(CreateLectureActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
//                FirebaseDatabase.getInstance().getReference("lectures").push().setValue(lecture).addOnSuccessListener(CreateLectureActivity.this, new OnSuccessListener<Void>() {
//
//                    @Override
//                    public void onSuccess(Void aVoid) {
//
//
//                        Intent intent = new Intent(CreateLectureActivity.this, MainActivity.class);
//                        Toast.makeText(CreateLectureActivity.this, "Lecture successfully created", Toast.LENGTH_SHORT).show();
//                        startActivity(intent);
//                    }
//                }).addOnFailureListener(CreateLectureActivity.this, new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(CreateLectureActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
            }
        }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), true);
        timePickerDialog.show(getFragmentManager(), "tag2");
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MAPS_PICKER_ACTIVITY_RESULT) {
            if(resultCode == RESULT_OK) {
                LatLng selectedPlace = data.getParcelableExtra("selected_place");
                String address = data.getStringExtra("selected_address");

                lecture.setLat(selectedPlace.latitude);
                lecture.setLng(selectedPlace.longitude);
                lecture.setAddress(address);

                placeSet = true;
                showDatePicker();
            }
        }
        else if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                return;
            }
            try {
                InputStream inputStream = CreateLectureActivity.this.getContentResolver().openInputStream(data.getData());
                ivPicture.setImageBitmap(BitmapFactory.decodeStream(inputStream));

            } catch (FileNotFoundException e) {
                Toast.makeText(CreateLectureActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
