package com.application.chatthingsup;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.net.URI;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    CircleImageView setProfile;
    EditText setName, setStatus;
    Button setButton;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    String email, password, profile, status, name;
    Uri setImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        setProfile = findViewById(R.id.userSettingProfile);
        setName = findViewById(R.id.settingUsername);
        setStatus = findViewById(R.id.settingStatus);
        setButton =findViewById(R.id.btnChange);

        DatabaseReference reference = database.getReference().child("user").child(auth.getUid());
        StorageReference storageReference = storage.getReference().child("upload").child(auth.getUid());

        // Load user data
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Check if the data exists before accessing it

               /* if (snapshot.exists()) {
                    DataSnapshot emailSnapshot = snapshot.child("email");
                    DataSnapshot passwordSnapshot = snapshot.child("password");
                    DataSnapshot nameSnapshot = snapshot.child("name");
                    DataSnapshot profileSnapshot = snapshot.child("profilePic");
                    DataSnapshot statusSnapshot = snapshot.child("status");

                    if (emailSnapshot.exists()) {
                        email = emailSnapshot.getValue().toString();
                    }else if (passwordSnapshot.exists()) {
                        password = passwordSnapshot.getValue().toString();
                    }else if (nameSnapshot.exists()) {
                        String name = nameSnapshot.getValue().toString();
                        setName.setText(name);
                    }else if (profileSnapshot.exists()) {
                        String profile = profileSnapshot.getValue().toString();
                        Picasso.get().load(profile).into(setProfile);
                    }else if (statusSnapshot.exists()) {
                        String status = statusSnapshot.getValue().toString();
                        setStatus.setText(status);
                    }
                }*/

                // Calling the data from database to local system

                email = snapshot.child("email").getValue().toString();
                password = snapshot.child("password").getValue().toString();
                name = snapshot.child("name").getValue().toString();
                status = snapshot.child("status").getValue().toString();
                profile = snapshot.child("profilePic").getValue().toString();


                //setting remote data to local system
                setName.setText(name);
                setStatus.setText(status);
                Picasso.get().load(profile).into(setProfile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });

        setProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 10);
            }
        });

        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = setName.getText().toString();
                String status = setStatus.getText().toString();

                // Check if image is selected
                if (setImageUri != null){
                    storageReference.putFile(setImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String finalImageUri = uri.toString();
                                        Users users = new Users(auth.getUid(), name, email,password,finalImageUri,status);
                                        reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    Toast.makeText(SettingsActivity.this, "Data has been updated", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    Toast.makeText(SettingsActivity.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                });
                            } else {
                                // Handle upload failure
                                Toast.makeText(SettingsActivity.this, "Upload failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    // If no new image is selected, update other information only
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String finalImageUri =uri.toString();
                            Users users = new Users(auth.getUid(), name, email,password,finalImageUri,status);

                            reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(SettingsActivity.this, "Data has been updated", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(SettingsActivity.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 10 && resultCode == RESULT_OK && data != null){
            setImageUri = data.getData();
            setProfile.setImageURI(setImageUri);
        }
    }
}
