package com.android.israr.myfirebasechatapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FriendsListActivity extends AppCompatActivity {
    List<User> usersList;
    FirebaseDatabase database;
    DatabaseReference usersRef;
    RecyclerView rvUsers;
    FriendsListAdapter adapter;
    Context context;
    UserSessionManager userSessionManager;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);
        context = this;
        rvUsers = findViewById(R.id.rvUsers);
        rvUsers.setLayoutManager(new LinearLayoutManager(context));
        database = FirebaseDatabase.getInstance();
        usersRef = Config.USERS_REF;
        usersList = new ArrayList<>();
        userSessionManager = new UserSessionManager(context);
        progressBar = findViewById(R.id.progressbar);
        showProgressBar();

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    User user = data.getValue(User.class);
                    if(user!=null){
                        if(!user.getUsername().equals(userSessionManager.getLoggedInUser().getUsername())){
                            usersList.add(user);
                        }
                    }
                }

                adapter = new FriendsListAdapter(usersList,context,R.layout.users_list_item);
                rvUsers.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                showList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showList() {
        progressBar.setVisibility(View.GONE);
        rvUsers.setVisibility(View.VISIBLE);
    }

    private void showProgressBar() {
        rvUsers.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }
}
