package com.android.israr.myfirebasechatapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;
import com.sinch.android.rtc.calling.CallListener;

import java.util.List;

public class CallActivity extends AppCompatActivity {
    private Call call;
    TextView callState;
    Button btnCall;
    UserSessionManager userSessionManager;
    String targetUsername;
    private final static int REQUEST_RECORD_AUDIO_PERMISSION = 22;
    SinchClient sinchClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        targetUsername = getIntent().getStringExtra("targetUsername");

        callState = findViewById(R.id.callState);
        userSessionManager = new UserSessionManager(this);

        sinchClient = Sinch.getSinchClientBuilder()
                .context(this)
                .userId(userSessionManager.getLoggedInUser().getUsername())
                .applicationKey("ffd3be37-9087-4144-8da0-21f8a3368933")
                .applicationSecret("nLoUxDIC8kmPH9+h5myEHg==")
                .environmentHost("sandbox.sinch.com")
                .build();

        sinchClient.setSupportCalling(true);
        sinchClient.start();

        sinchClient.startListeningOnActiveConnection();
        sinchClient.getCallClient().addCallClientListener(new SinchCallClientListener());

        btnCall = findViewById(R.id.btnCall);
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (call == null) {
                    if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(CallActivity.this,new String[]{Manifest.permission.RECORD_AUDIO},REQUEST_RECORD_AUDIO_PERMISSION);
                    }else {
                        setCall();
                    }
                } else {
                    call.hangup();
                }
            }
        });
    }

    private class SinchCallListener implements CallListener {
        @Override
        public void onCallEnded(Call endedCall) {
            //call ended by either party
            call = null;
            btnCall.setText("Call");
            setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
            callState.setText("");
        }

        @Override
        public void onCallEstablished(Call establishedCall) {
            //incoming call was picked up
            setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
            callState.setText("connected");
        }

        @Override
        public void onCallProgressing(Call progressingCall) {
            //call is ringing
            callState.setText("Ringing...");
        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> pushPairs) {
            //don't worry about this right now
        }
    }

    private class SinchCallClientListener implements CallClientListener {
        @Override
        public void onIncomingCall(CallClient callClient, Call incomingCall) {
            //Pick up the call!
            call = incomingCall;
            call.answer();
            call.addCallListener(new SinchCallListener());
            btnCall.setText("Hang Up");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    setCall();
                }else{
                    Toast.makeText(this, "Permission not granted!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void setCall() {
        call = sinchClient.getCallClient().callUser(targetUsername);
        call.addCallListener(new SinchCallListener());
        btnCall.setText("Hang up");
    }
}
