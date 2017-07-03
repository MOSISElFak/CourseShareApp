package com.example.stefanzivic.courseshare.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stefanzivic.courseshare.R;
import com.example.stefanzivic.courseshare.adapters.holders.UserViewHolder;
import com.example.stefanzivic.courseshare.model.User;

import java.util.List;

/**
 * Created by Ivan on 7/3/2017.
 */

public class RecyclerUserAdapter extends RecyclerView.Adapter<UserViewHolder> {

    private List<User> users;

    private OnUserClickListener listener;

    public RecyclerUserAdapter(List<User> users, OnUserClickListener listener) {
        this.users = users;

        this.listener = listener;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_user, parent, false);
        return new UserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final UserViewHolder holder, int position) {
        String name = users.get(position).getName();
        holder.setName(name);

        String description = users.get(position).getInfo();
        holder.setInfo(description);

        String picture = users.get(position).getPicture();
        holder.setPicture(picture, name);

        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onUserClick(users.get(holder.getAdapterPosition()).getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
