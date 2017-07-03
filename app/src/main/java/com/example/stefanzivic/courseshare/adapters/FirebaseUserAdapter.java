package com.example.stefanzivic.courseshare.adapters;
//
//import android.content.Context;
//import android.graphics.Color;
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//import com.example.stefanzivic.courseshare.R;
//import com.example.stefanzivic.courseshare.adapters.holders.UserViewHolder;
//import com.example.stefanzivic.courseshare.model.User;
//import com.firebase.ui.database.FirebaseRecyclerAdapter;
//import com.firebase.ui.storage.images.FirebaseImageLoader;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//
///**
// * Created by Ivan on 7/3/2017.
// */
//

public class FirebaseUserAdapter {

}
//public class FirebaseUserAdapter extends FirebaseRecyclerAdapter<User, UserViewHolder> {
//
//    private OnUserClickListener listener;
//
//    public FirebaseUserAdapter(DatabaseReference ref, OnUserClickListener listener) {
//        super(User.class, R.layout.row_user, UserViewHolder.class, ref);
//
//        this.listener = listener;
//    }
//
//
//    @Override
//    protected void populateViewHolder(final UserViewHolder viewHolder, User model, int position) {
//        String name = model.getName();
//        viewHolder.setName(name);
//
//        String description = model.getInfo();
//        viewHolder.setInfo(description);
//
//        String picture = model.getPicture();
//        viewHolder.setPicture(picture, name);
//
//        viewHolder.getView().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DatabaseReference testRef = getRef(viewHolder.getAdapterPosition());
//                listener.onUserClick(testRef.getKey());
//            }
//        });
//    }
//}
