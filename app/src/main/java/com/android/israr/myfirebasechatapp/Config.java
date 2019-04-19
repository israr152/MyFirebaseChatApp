package com.android.israr.myfirebasechatapp;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Config {
    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static final DatabaseReference USERS_REF = database.getReference("Users");
    public static final DatabaseReference CONVO_REF = database.getReference("Conversations");
}
