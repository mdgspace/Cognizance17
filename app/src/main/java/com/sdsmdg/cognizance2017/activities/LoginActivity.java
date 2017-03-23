package com.sdsmdg.cognizance2017.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sdsmdg.cognizance2017.R;
import com.sdsmdg.cognizance2017.SessionManager;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static com.sdsmdg.cognizance2017.activities.MainActivity.BASE_URL;

public class LoginActivity extends AppCompatActivity {
    private String userName;
    private String userCogniId;
    EditText nameEditText;
    EditText passwordEditText;
    SessionManager session;
    DataInterface api;
    Button button;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        session = new SessionManager(getApplicationContext());
        nameEditText = (EditText) findViewById(R.id.userName);
        passwordEditText = (EditText) findViewById(R.id.userPhoneNumber);
        button = (Button) findViewById(R.id.loginButton);
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(BASE_URL)
                .build();
        api = adapter.create(DataInterface.class);


        Toast.makeText(this, "User Login Status :" + session.isLoggedIn(), Toast.LENGTH_SHORT).show();
        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setMessage("Checking Login Credentials");
                progressDialog.show();
                checkUser();
            }
        });

    }

    private void setUser() {


        if (userName.trim().length() > 0 && userCogniId.trim().length() > 0) {

            //creating user login session
            session.createLoginSession(userName, userCogniId);
        }
        else
            Toast.makeText(this, "Login failed due to empty fields", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void checkUser(){
        //get username and usernumber from editText
        userName = nameEditText.getText().toString();
        userCogniId = passwordEditText.getText().toString();
        api.checkUser(userCogniId, new Callback<String>() {

            @Override
            public void success(String s, Response response) {
                if (s.equals("200")) {
                    progressDialog.dismiss();
                    setUser();
                }
                else if (s.equals("401")) {
                    Snackbar.make(button, "Incorrect Cognizance Id", Snackbar.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }

            }

            @Override
            public void failure(RetrofitError error) {
                Snackbar.make(button,"Network error",Snackbar.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        });

    }
}
