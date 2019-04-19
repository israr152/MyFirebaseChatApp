package com.android.israr.myfirebasechatapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AlphabetIndexer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    EditText etTextMessage;
    ImageButton btnSend;
    RecyclerView rvChatMessages;
    Context context;
    FirebaseDatabase database;
    DatabaseReference chatRef,targetUserRef;
    ChatListAdapter adapter;
    List<ChatMessage> chatMessageList;
    static String myUsername, targetUser,targetUsername,myFullname;
    UserSessionManager userSessionManager;
    Conversation convo1,convo2;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        rvChatMessages = findViewById(R.id.rvChatMessages);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        rvChatMessages.setLayoutManager(linearLayoutManager);
        etTextMessage = findViewById(R.id.etTextMessage);
        btnSend = findViewById(R.id.btnSend);
        context = this;
        progressBar = findViewById(R.id.progressbar);
        chatMessageList = new ArrayList<>();
        userSessionManager = new UserSessionManager(context);
        database = FirebaseDatabase.getInstance();
        showProgressBar();

        targetUser = getIntent().getStringExtra("targetUser");
        targetUsername = getIntent().getStringExtra("targetUsername");
        myUsername = userSessionManager.getLoggedInUser().getUsername();
        myFullname = userSessionManager.getLoggedInUser().getFullName();

        chatRef = Config.CONVO_REF.child(myUsername);
        targetUserRef = Config.CONVO_REF.child(targetUsername);

        setTitle(targetUser);

        setChatMessageList();
        setupSendButton();
    }

    private void showProgressBar() {
        rvChatMessages.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void setupSendButton() {
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etTextMessage.getText().toString().equals("")){
                    return;
                }

                convo1 = new Conversation();
                convo2 = new Conversation();

                ChatMessage message = new ChatMessage();
                message.setMessage(etTextMessage.getText().toString());
                message.setSenderName(myFullname);
                chatMessageList.add(message);

                convo1.setConversationName(targetUser);
                convo1.setConversationUsername(targetUsername);
                convo1.setMessages(chatMessageList);

                convo2.setConversationName(myFullname);
                convo2.setConversationUsername(myUsername);
                convo2.setMessages(chatMessageList);

                chatRef.child(targetUsername).setValue(convo1);

                targetUserRef.child(myUsername).setValue(convo2).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            etTextMessage.setText("");
                            Toast.makeText(getApplicationContext(), "Message Sent!", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(), "Message not Sent!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void setAdapter() {
        adapter = new ChatListAdapter(chatMessageList,targetUser,context,R.layout.chat_item_layout);
        rvChatMessages.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void showList() {
        rvChatMessages.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    private void setChatMessageList() {
        chatRef.child(targetUsername).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                convo1 = dataSnapshot.getValue(Conversation.class);
                if(convo1!=null){
                    chatMessageList.clear();
                    chatMessageList.addAll(convo1.getMessages());
                    setAdapter();
                }
                showList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
