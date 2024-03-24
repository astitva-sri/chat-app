package com.application.chatthingsup;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.zegocloud.uikit.prebuilt.call.invite.widget.ZegoSendCallInvitationButton;
import com.zegocloud.uikit.service.defines.ZegoUIKitUser;

import java.util.Collections;

public class VideoCallReceiverProfile extends AppCompatActivity {

    TextView caller;
    EditText targetUser;
    ZegoSendCallInvitationButton callBtn;
    ImageView backBtn;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_video_call_receiver_profile);

        callBtn = findViewById(R.id.btnConnectVC);
        targetUser = findViewById(R.id.inputReceiverId);
        caller = findViewById(R.id.hostId);
        backBtn = findViewById(R.id.backBtn);

        caller.setText("You are: " + getIntent().getStringExtra("caller"));

        targetUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                startVideoCall(targetUser.getText().toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VideoCallReceiverProfile.this, VideoCallActivity.class);
                startActivity(intent);
            }
        });
    }

    public void startVideoCall(String targetUserID){
        callBtn.setIsVideoCall(true);
        callBtn.setResourceID("zego_uikit_call");
        callBtn.setInvitees(Collections.singletonList(new ZegoUIKitUser(targetUserID,targetUserID)));
    }
}