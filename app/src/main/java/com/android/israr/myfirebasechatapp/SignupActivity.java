package com.android.israr.myfirebasechatapp;

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

public class SignupActivity extends AppCompatActivity {
    EditText etEmail,etPassword,etFullName;
    Button btnSignup;
    String email,password,fullName;
    FirebaseDatabase database;
    DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("Users");
        etFullName = findViewById(R.id.etFullName);
        btnSignup = findViewById(R.id.btnSignup);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = etEmail.getText().toString();
                password = etPassword.getText().toString();
                fullName = etFullName.getText().toString();
                if(!validateFields(fullName, email,password)){
                    if(fullName.equals("")){
                        etFullName.setError("enter name");
                    }
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
                signup(fullName, email,password);
            }
        });
    }

    private boolean validateFields(String fullName, String email, String password) {
        return !fullName.equals("") && !email.equals("") && !password.equals("");
    }

    private void signup(String fullName, final String email, String password) {
        final User user = new User(email,password);
        user.setFullName(fullName);
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String username = user.getEmail().replace("@","");
                username = username.replace(".","");
                user.setUsername(username);

                if(dataSnapshot.child(username).exists()){
                    Toast.makeText(getApplicationContext(), "User already exists!", Toast.LENGTH_SHORT).show();
                }else{
                    usersRef.child(username).setValue(user);
                    Toast.makeText(getApplicationContext(), "User registered!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private static boolean isValidEmail(String email) {
        return TextUtils.isEmpty(email) || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
