package com.android.israr.myfirebasechatapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.TextView;

class UserSessionManager {
    private SharedPreferences userDB;

    UserSessionManager(Context ctx){
        userDB = ctx.getSharedPreferences(ctx.getString(R.string.userDB),Context.MODE_PRIVATE);
    }

    void storeUserData(User user){
        SharedPreferences.Editor editor = userDB.edit();
        editor.putString("email",user.getEmail());
        editor.putString("password",user.getPassword());
        editor.putString("name",user.getFullName());
        editor.putString("myUsername",user.getUsername());
        editor.putBoolean("userLogged",true);
        editor.apply();
    }

    User getLoggedInUser(){
        User user = new User(userDB.getString("email",""),userDB.getString("password",""));
        user.setFullName(userDB.getString("name",""));
        user.setUsername(userDB.getString("myUsername",""));
        return user;
    }

    void clearUserData(){
        SharedPreferences.Editor editor = userDB.edit();
        editor.clear();
        editor.apply();
    }

    boolean isUserLogged(){
        return userDB.getBoolean("userLogged",false);
    }
}
