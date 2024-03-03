package com.application.chatthingsup;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdaptor extends RecyclerView.Adapter<UserAdaptor.viewholder> {

    MainActivity mainActivity;
    ArrayList<Users> usersArrayList;
    public UserAdaptor(MainActivity mainActivity, ArrayList<Users> usersArrayList) {
        this.mainActivity = mainActivity;
        this.usersArrayList = usersArrayList;
    }

    @NonNull
    @Override
    public UserAdaptor.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mainActivity).inflate(R.layout.activity_user_profile, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdaptor.viewholder holder, int position) {

        Users users = usersArrayList.get(position);
        holder.userName.setText(users.name);
        holder.userStatus.setText(users.status);
        Picasso.get().load(users.profilePic).into(holder.userProfileImage);
    }

    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        CircleImageView userProfileImage;
        TextView userName, userStatus;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            userProfileImage = itemView.findViewById(R.id.userProfileImage);
            userName = itemView.findViewById(R.id.userName);
            userStatus = itemView.findViewById(R.id.userStatus);
        }
    }
}
