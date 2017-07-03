package com.example.stefanzivic.courseshare.adapters.holders;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.Glide;
import com.example.stefanzivic.courseshare.R;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by Ivan on 7/3/2017.
 */

public class LectureViewHolder extends RecyclerView.ViewHolder{

    private ImageView ivPicture;
    private TextView tvName;
    private TextView tvDescription;

    private View view;

    public LectureViewHolder(View itemView) {
        super(itemView);

        ivPicture = (ImageView) itemView.findViewById(R.id.row_lecture_picture);
        tvName = (TextView) itemView.findViewById(R.id.row_lecture_name);
        tvDescription = (TextView) itemView.findViewById(R.id.row_lecture_description);

        view = itemView;
    }

    public View getView() { return this.view; }

    public void setName(String name) {
        if (name != null) {
            if (name.length() > 140) {
                String pom = name.substring(0, 137) + "...";
                tvName.setText(pom);
            } else {
                tvName.setText(name);
            }
        }
        else {
            tvName.setText("<Lecture has no name>");
        }
    }

    public void setDescription(String description) {
        if (description != null) {
            if (description.length() > 140) {
                String pom = description.substring(0, 137) + "...";
                tvDescription.setText(pom);
            } else {
                tvDescription.setText(description);
            }
        }
        else {
            tvDescription.setText("<Lecture has no description>");
        }
    }

    public void setPicture(String picture, String name) {
        if (picture != null) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference ref = storage.getReference().child(picture);

            Glide.with(view.getContext()).using(new FirebaseImageLoader()).load(ref).into(ivPicture);
        }
        else {
            ivPicture.setImageResource(0);
            TextDrawable drawable = TextDrawable.builder()
                    .buildRoundRect(name.substring(0, 1), Color.DKGRAY, 16);
            ivPicture.setImageDrawable(drawable);
        }

    }
}
