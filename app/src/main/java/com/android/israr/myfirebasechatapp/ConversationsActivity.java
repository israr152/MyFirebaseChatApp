package com.android.israr.myfirebasechatapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ConversationsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    RecyclerView rvConversations;
    FirebaseDatabase database;
    DatabaseReference conRef;
    UserSessionManager userSessionManager;
    List<Conversation> conList;
    Context context;
    Activity myActivity;
    ConversationAdapter adapter;
    TextView tvNoConvo;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressBar = findViewById(R.id.progressbar);

        rvConversations = findViewById(R.id.rvConversations);
        tvNoConvo = findViewById(R.id.tvNoConversations);
        rvConversations.setLayoutManager(new LinearLayoutManager(this));
        showProgressBar();

        context = this;
        conList = new ArrayList<>();
        userSessionManager = new UserSessionManager(this);
        database = FirebaseDatabase.getInstance();
        String username = userSessionManager.getLoggedInUser().getUsername();
        conRef = Config.CONVO_REF.child(username);

        conRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(conList!=null){
                    conList.clear();
                }
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    Conversation conversation = data.getValue(Conversation.class);
                    conList.add(conversation);
                }
                setAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToFriendsActivity();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void showProgressBar() {
        rvConversations.setVisibility(View.GONE);
        tvNoConvo.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

    }

    private void setAdapter() {
        if(conList.size()==0){
            showNoConversationsText();
        }else{
            adapter = new ConversationAdapter(context,R.layout.conversation_list_item,conList);
            rvConversations.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            showList();
        }
    }

    private void showList() {
        rvConversations.setVisibility(View.VISIBLE);
        tvNoConvo.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    private void showNoConversationsText() {
        rvConversations.setVisibility(View.GONE);
        tvNoConvo.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_users) {
            goToFriendsActivity();
        } else if (id == R.id.nav_logout) {
            userSessionManager.clearUserData();
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                finishAffinity();
            }else{
                ActivityCompat.finishAffinity(myActivity);
            }
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void goToFriendsActivity() {
        startActivity(new Intent(ConversationsActivity.this,FriendsListActivity.class));
    }
}
