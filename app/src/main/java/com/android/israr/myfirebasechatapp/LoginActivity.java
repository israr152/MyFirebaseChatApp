package com.android.israr.myfirebasechatapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    EditText etEmail,etPassword;
    Button btnLogin,btnSignup;
    String email,password;
    FirebaseDatabase database;
    DatabaseReference usersRef;
    UserSessionManager userSessionManager;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference();
        btnLogin = findViewById(R.id.btnLogin);
        btnSignup = findViewById(R.id.btnSignup);
        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("Users");
        context = this;
        userSessionManager = new UserSessionManager(context);

        if(userSessionManager.isUserLogged()){
            goToMainActivity();
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = etEmail.getText().toString();
                password = etPassword.getText().toString();
                if(!validateFields(email,password)){
                    if(email.equals("")){
                        etEmail.setError("enter email");
                    }else if(!isValidEmail(email)){
                        etEmail.setError("invalid email");
                    }
                    if(password.equals("")){
                        etPassword.setError("enter password");
                    }
                    return;
                }
                login(email,password);
            }
        });
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,SignupActivity.class));
            }
        });
    }

    private void goToMainActivity() {
        startActivity(new Intent(LoginActivity.this,ConversationsActivity.class));
    }

    private void login(final String email, final String password) {
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String username = email.replace("@","");
                username = username.replace(".","");

                if(!dataSnapshot.child(username).exists()){
                    Toast.makeText(getApplicationContext(), "User is not registered!", Toast.LENGTH_SHORT).show();
                }else{
                    User login = dataSnapshot.child(username).getValue(User.class);
                    if(login!=null)
                    if(login.getEmail().equals(email) && login.getPassword().equals(password)){
                        userSessionManager.storeUserData(login);
                        goToMainActivity();
                    }else{
                        Toast.makeText(getApplicationContext(), "Email or Password is incorrect!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateFields(String email, String password) {
        return !email.equals("") && !password.equals("");
    }

    private static boolean isValidEmail(String email) {
        return TextUtils.isEmpty(email) || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}
