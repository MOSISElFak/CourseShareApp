package com.example.stefanzivic.courseshare.adapters;

import android.view.View;

import com.example.stefanzivic.courseshare.R;
import com.example.stefanzivic.courseshare.adapters.holders.LectureViewHolder;
import com.example.stefanzivic.courseshare.model.Lecture;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by Ivan on 7/3/2017.
 */

public class FirebaseLectureAdapter extends FirebaseRecyclerAdapter<Lecture, LectureViewHolder> {

    private OnLectureClickListener listener;

    public FirebaseLectureAdapter(DatabaseReference ref, OnLectureClickListener listener) {
        super(Lecture.class, R.layout.row_lecture, LectureViewHolder.class, ref);

        this.listener = listener;
    }

    @Override
    protected void populateViewHolder(final LectureViewHolder viewHolder, Lecture model, int position) {
        String name = model.getName();
        viewHolder.setName(name);

        String description = model.getDescription();
        viewHolder.setDescription(description);

        String picture = model.getPicture();
        viewHolder.setPicture(picture, name);

        viewHolder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference testRef = getRef(viewHolder.getAdapterPosition());
                listener.onLectureClick(testRef.getKey());
            }
        });
    }

}
