package com.sdsmdg.cognizance2017.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sdsmdg.cognizance2017.R;
import com.sdsmdg.cognizance2017.SessionManager;

import static com.sdsmdg.cognizance2017.activities.MainActivity.mainAct;

public class LoginActivity extends AppCompatActivity {
    private String userName;
    private String userNumber;
    EditText nameEditText;
    EditText passwordEditText;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        session = new SessionManager(getApplicationContext());
        nameEditText = (EditText) findViewById(R.id.userName);
        passwordEditText = (EditText) findViewById(R.id.userPhoneNumber);


        Toast.makeText(this, "User Login Status :" + session.isLoggedIn(), Toast.LENGTH_SHORT).show();
        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUser();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });

    }

    private void setUser() {
        //get username and usernumber from editText
        userName = nameEditText.getText().toString();
        userNumber = passwordEditText.getText().toString();
        if (userName.trim().length() > 0 && userNumber.trim().length() > 0) {

            //creating user login session
            session.createLoginSession(userName,userNumber);
        }
        else
            Toast.makeText(this, "Login failed due to empty fields", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mainAct.finish();
    }
}
