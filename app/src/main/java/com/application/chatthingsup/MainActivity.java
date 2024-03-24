package com.application.chatthingsup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // UI components
    ImageView btnSignOut, btnSettings, btnVideoCall;

    // Firebase
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference usersReference;

    // RecyclerView for displaying users
    RecyclerView mainUserRecyclerView;
    UserAdaptor userAdapter;
    ArrayList<Users> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        btnSignOut = findViewById(R.id.btnSignOut);
        btnSettings = findViewById(R.id.btnSettings);
        btnVideoCall = findViewById(R.id.btnVideoCall);

        // Initialize Firebase
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        DatabaseReference reference = database.getReference().child("user");

        // Initialize RecyclerView for displaying users
        userList = new ArrayList<>();
        mainUserRecyclerView = findViewById(R.id.mainUserRecyclerView);
        mainUserRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        userAdapter = new UserAdaptor(MainActivity.this, userList);
        mainUserRecyclerView.setAdapter(userAdapter);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Users users = dataSnapshot.getValue(Users.class);
                    userList.add(users);
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Sign-out button on click listener
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignOutDialog();
            }
        });

        // Set onClickListener for settings button
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettingsActivity();
            }
        });

        // Set onClickListener for VideoCall button
        btnVideoCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openVideoCallActivity();
            }
        });

        if (auth.getCurrentUser() == null){
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    // Show sign out confirmation dialog
    private void showSignOutDialog() {
        Dialog dialog = new Dialog(MainActivity.this, R.style.Dialog);
        dialog.setContentView(R.layout.dialog_layout);

        Button yes = dialog.findViewById(R.id.btnYesLogout);
        Button no = dialog.findViewById(R.id.btnNoLogout);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish(); // Finish the activity after sign out
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    // Open SettingsActivity
    private void openSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    // Open VideoCallActivity
    private void openVideoCallActivity(){
        Intent intent = new Intent(this, VideoCallActivity.class);
        startActivity(intent);
    }
}
