package com.example.stefanzivic.courseshare.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stefanzivic.courseshare.R;
import com.example.stefanzivic.courseshare.adapters.holders.LectureViewHolder;
import com.example.stefanzivic.courseshare.model.Lecture;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

/**
 * Created by Ivan on 7/3/2017.
 */

public class RecyclerLectureAdapter extends RecyclerView.Adapter<LectureViewHolder> {

    private List<Lecture> lectures;
    private OnLectureClickListener listener;

    public RecyclerLectureAdapter(List<Lecture> lectures, OnLectureClickListener listener) {
        this.lectures = lectures;

        this.listener = listener;
    }

    @Override
    public LectureViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_lecture, parent, false);
        return new LectureViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final LectureViewHolder holder, int position) {
        String name = lectures.get(position).getName();
        holder.setName(name);

        String description = lectures.get(position).getDescription();
        holder.setDescription(description);

        String picture = lectures.get(position).getPicture();
        holder.setPicture(picture, name);

        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onLectureClick(lectures.get(holder.getAdapterPosition()).getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return lectures.size();
    }

}
