package com.application.chatthingsup;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdaptor extends RecyclerView.Adapter<UserAdaptor.Viewholder> {

    Context mainActivity;
    ArrayList<Users> usersArrayList;
    public UserAdaptor(MainActivity mainActivity, ArrayList<Users> usersArrayList) {
        this.mainActivity = mainActivity;
        this.usersArrayList = usersArrayList;
    }

    @NonNull
    @Override
    public UserAdaptor.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mainActivity).inflate(R.layout.user_item_view, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdaptor.Viewholder holder, int position) {

        Users users = usersArrayList.get(position);
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(users.getUserId())){
            holder.itemView.setVisibility(View.GONE);
        }
        holder.userName.setText(users.name);
        holder.userStatus.setText(users.status);
        Picasso.get().load(users.profilePic).into(holder.userProfileImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainActivity, ChatWindow.class);
                intent.putExtra("nameeee", users.getName());
                intent.putExtra("recevierImg", users.getProfilePic());
                intent.putExtra("uid", users.getUserId());
                intent.putExtra("statusWin", users.getStatus());
                mainActivity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        CircleImageView userProfileImage;
        TextView userName, userStatus;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            userProfileImage = itemView.findViewById(R.id.userProfileImage);
            userName = itemView.findViewById(R.id.userName);
            userStatus = itemView.findViewById(R.id.userStatus);
        }
    }
}
