package com.application.chatthingsup;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.zegocloud.uikit.prebuilt.call.config.ZegoNotificationConfig;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationService;

public class VideoCallActivity extends AppCompatActivity {

    ImageView backBtn;
    EditText inputHostId;
    Button loginBtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_video_call);

        backBtn = findViewById(R.id.backHomeBtn);
        inputHostId =findViewById(R.id.inputHostId);
        loginBtn = findViewById(R.id.btnLoginToNext);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(inputHostId.getText())){
                    Toast.makeText(VideoCallActivity.this, "Enter a valid email", Toast.LENGTH_SHORT).show();
                    return;
                }
                startMyService(inputHostId.getText().toString());
                Intent intent = new Intent(getApplicationContext(), VideoCallReceiverProfile.class);
                intent.putExtra("caller", inputHostId.getText().toString().trim());
                startActivity(intent);
            }
        });


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VideoCallActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void startMyService(String userId){
        Application application = getApplication(); // Android's application context
        long appID = 1753069781;   // yourAppID
        String appSign = "a447c85bc4f6c3a4ffd087fbe656fff0f15fbd5a1de9fadd3878b488b5832a94";  // yourAppSign
        String userID = userId; // yourUserID, userID should only contain numbers, English characters, and '_'.
        String userName = userId;   // yourUserName

        ZegoUIKitPrebuiltCallInvitationConfig callInvitationConfig = new ZegoUIKitPrebuiltCallInvitationConfig();

        ZegoNotificationConfig notificationConfig = new ZegoNotificationConfig();
        notificationConfig.sound = "zego_uikit_sound_call";
        notificationConfig.channelID = "CallInvitation";
        notificationConfig.channelName = "CallInvitation";

        ZegoUIKitPrebuiltCallInvitationService.init(getApplication(), appID, appSign, userID, userName,callInvitationConfig);
    }

    public void onDestroy(){
        super.onDestroy();
        ZegoUIKitPrebuiltCallInvitationService.unInit();

    }
}