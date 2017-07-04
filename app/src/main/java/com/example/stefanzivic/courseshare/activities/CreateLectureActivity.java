package com.example.stefanzivic.courseshare.activities;

import android.app.DatePickerDialog;
import android.icu.util.Calendar;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.stefanzivic.courseshare.R;
import com.example.stefanzivic.courseshare.model.Lecture;

public class CreateLectureActivity extends AppCompatActivity {

    private boolean placeSet;
    private boolean dateSet;
    private boolean timeSet;

    ImageView ivPicture;

    EditText etName;
    EditText etDescription;

    Button bPicture;
    Button bCreate;
    Button bCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_lecture);

        placeSet = false;
        dateSet = false;
        timeSet = false;

        ivPicture = (ImageView) findViewById(R.id.activity_create_lecture_picture);
        etName = (EditText) findViewById(R.id.activity_create_lecture_name);
        etDescription = (EditText) findViewById(R.id.activity_create_lecture_description);

        bPicture = (Button) findViewById(R.id.activity_edit_profile_picture_button);
        bCreate = (Button) findViewById(R.id.activity_create_lecture_create_button);
        bCancel = (Button) findViewById(R.id.activity_create_lecture_cancel_button);

        bCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etName.getText().toString().isEmpty()) {
                    Lecture lecture = new Lecture();
                    lecture.setName(etName.getText().toString());
                    if (!etDescription.getText().toString().isEmpty()) {
                        lecture.setDescription(etDescription.getText().toString());
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
    }

    public void showPlacePicker() {

    }

    public void showDatePicker() {

//        final Calendar c = Calendar.getInstance();
//        int mYear = c.get(Calendar.YEAR);
//        int mMonth = c.get(Calendar.MONTH);
//        int mDay = c.get(Calendar.DAY_OF_MONTH);
//
//        DatePickerDialog datePickerDialog = new DatePickerDialog(CreateLectureActivity.this, new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//
//            }
//        });
    }

    public void showTimePicker() {

    }


}
