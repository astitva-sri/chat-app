package com.application.chatthingsup;

import static com.application.chatthingsup.ChatWindow.receiverIImg;
import static com.application.chatthingsup.ChatWindow.senderImg;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

public class MessagesAdaptor extends RecyclerView.Adapter {

    Context context;
    ArrayList<MessageModelClass> messageAdaptorArrayList;
    int itemSent = 1;
    int itemReceived = 2;

    public MessagesAdaptor(Context context, ArrayList<MessageModelClass> messageAdaptorArrayList) {
        this.context = context;
        this.messageAdaptorArrayList = messageAdaptorArrayList;   /*!= null ? messageAdaptorArrayList : new ArrayList<>();*/
    }

    //Declaring View holder for sender profile
    static class senderViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profileImage;
        TextView msgText;
        public senderViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.senderProfileImage);
            msgText = itemView.findViewById(R.id.msgSenderText);
        }
    }

    //Declaring View holder for receiver profile
    static class receiverViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profileImage;
        TextView msgText;
        public receiverViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.receiverProfileImage);
            msgText = itemView.findViewById(R.id.receiverTextMessage);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == itemSent){
            View view  = LayoutInflater.from(context).inflate(R.layout.sender_layout, parent, false);
            return new senderViewHolder(view);
        }else {
            View view = LayoutInflater.from(context).inflate(R.layout.receiver_layout, parent, false);
            return new receiverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageModelClass messages = messageAdaptorArrayList.get(position);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(context).setTitle("Delete")
                        .setMessage("Are you sure you want to delete this message?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();

                return false;
            }
        });

        if (holder.getClass() == senderViewHolder.class){
            senderViewHolder viewHolder = (senderViewHolder) holder;
            viewHolder.msgText.setText(messages.getMessage());
            Picasso.get().load(senderImg).into(viewHolder.profileImage);
        } else {
            receiverViewHolder viewHolder = (receiverViewHolder) holder;
            viewHolder.msgText.setText(messages.getMessage());
            Picasso.get().load(receiverIImg).into(viewHolder.profileImage);
        }
    }

    @Override
    public int getItemCount() {
        return messageAdaptorArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        MessageModelClass message = messageAdaptorArrayList.get(position);
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(message.getSenderId())){
            return itemSent;
        }else {
            return itemReceived;
        }
    }
}
