package com.application.chatthingsup;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatWindow extends AppCompatActivity {

    // Declare variables
    String receiverImg, receiverUid, receiverName, senderUid, senderRoom, receiverRoom;
    CircleImageView profile;
    TextView receiverNName, receiverSStatus;
    EditText writeTextMsg;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    RecyclerView msgAdapterView;
    CardView sendBtn;
    ArrayList<MessageModelClass> messagesArrayList;
    MessagesAdaptor messagesAdaptor;
    public static String senderImg, receiverIImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        // Initialize Firebase instances
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        // Extract data from intent
        receiverName = getIntent().getStringExtra("nameeee");
        receiverImg = getIntent().getStringExtra("recevierImg");
        receiverUid = getIntent().getStringExtra("uid");

        // Initialize messages ArrayList
        messagesArrayList = new ArrayList<>();

        // Initialize views
        sendBtn = findViewById(R.id.sendBtn);
        profile = findViewById(R.id.userProfileImage);
        receiverNName = findViewById(R.id.receiverName);
        receiverSStatus = findViewById(R.id.receiverStatus);
        writeTextMsg = findViewById(R.id.writeTextMsg);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);

        //Initialize Adapters
        msgAdapterView = findViewById(R.id.msgAdapter);
        messagesAdaptor = new MessagesAdaptor(this, messagesArrayList);
        msgAdapterView.setLayoutManager(new LinearLayoutManager(this));
        msgAdapterView.setAdapter(messagesAdaptor);

        Picasso.get().load(receiverImg).into(profile);
        receiverNName.setText("" + receiverName);

        // Construct sender and receiver room IDs
        senderUid = firebaseAuth.getUid();
        senderRoom = senderUid + receiverUid;
        receiverRoom = receiverUid + senderUid;

        DatabaseReference reference = database.getReference().child("user").child(firebaseAuth.getUid());
        DatabaseReference chatReference = database.getReference().child("chats").child(senderRoom).child("messages");


        chatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MessageModelClass messages = dataSnapshot.getValue(MessageModelClass.class);
                    messagesArrayList.add(messages);
                }
                messagesAdaptor.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                senderImg = snapshot.child("profilePic").getValue().toString();
                receiverIImg = receiverImg;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = writeTextMsg.getText().toString();

                if (message.isEmpty()) {
                    Toast.makeText(ChatWindow.this, "Please enter the message First...", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    writeTextMsg.setText("");
                    Date date = new Date();
                    MessageModelClass messagess = new MessageModelClass(message, senderUid, date.getTime());

                    database = FirebaseDatabase.getInstance();
                    database.getReference()
                            .child("chats").child(senderRoom).child("messages")
                            .push().setValue(messagess)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    database.getReference()
                                            .child("chats").child(receiverRoom).child("messages")
                                            .push().setValue(messagess)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                }
                                            });
                                }
                            });
                }
            }
        });
    }
}

/* ---------------------------Extra code FOR experiment purpose Above is the active code------------------------------------*/









   /*



        // Load receiver information and messages
        loadReceiverInfo();
        loadMessages();

        // Set click listener for send button
        findViewById(R.id.sendBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    // Method to load receiver information
    private void loadReceiverInfo() {
        receiverNName.setText(receiverName);
        // Load receiver profile image using Picasso library if needed
        Picasso.get().load(receiverImg).into(profile);
        // Load receiver status if needed
    }

    // Method to load messages from Firebase database
    private void loadMessages() {
        DatabaseReference chatReference = database.getReference().child("chats").child(senderRoom).child("message");
        chatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MessageModelClass message = dataSnapshot.getValue(MessageModelClass.class);
                    messagesArrayList.add(message);
                }
                messagesAdaptor.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });
    }

    // Method to send message to Firebase database
    private void sendMessage() {
        String messageText = writeTextMsg.getText().toString().trim();
        if (!messageText.isEmpty()) {
            // Create message object with current timestamp
            Date date = new Date();
            MessageModelClass message = new MessageModelClass(messageText, senderUid, date.getTime());
            // Push message to sender and receiver rooms in Firebase database
            DatabaseReference senderReference = database.getReference().child("chats").child(senderRoom).child("message");
            senderReference.push().setValue(message);

            DatabaseReference receiverReference = database.getReference().child("chats").child(receiverRoom).child("message");
            receiverReference.push().setValue(message);

            // Clear message input field after sending
            writeTextMsg.setText("");
        } else {
            // Display toast if message input is empty
            Toast.makeText(this, "Enter the message first", Toast.LENGTH_SHORT).show();
        }
    }
}
*/