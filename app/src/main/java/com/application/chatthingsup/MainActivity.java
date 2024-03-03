package com.application.chatthingsup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    ImageView btnSignOut;
    FirebaseAuth auth;
    RecyclerView mainUserRecyclerView;
    UserAdaptor userAdaptor;
    FirebaseDatabase database;
    ArrayList<Users> usersArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSignOut = findViewById(R.id.btnSignOut);

        mainUserRecyclerView = findViewById(R.id.mainUserRecyclerView);
        mainUserRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mainUserRecyclerView.setAdapter(userAdaptor);

        userAdaptor = new UserAdaptor(MainActivity.this, usersArrayList);

        database = FirebaseDatabase.getInstance();

        DatabaseReference reference = database.getReference().child("user");

        usersArrayList = new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Users users =dataSnapshot.getValue(Users.class);
                    usersArrayList.add(users);
                }
                 userAdaptor.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null){
            Intent intent =new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
            }
        });



    }
}