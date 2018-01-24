package com.example.fotij.userapplication.controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.disklrucache.DiskLruCache;
import com.example.fotij.userapplication.R;
import com.example.fotij.userapplication.model.backend.BackEndFunc;
import com.example.fotij.userapplication.model.backend.DataSourceType;
import com.example.fotij.userapplication.model.backend.FactoryMethod;
import com.example.fotij.userapplication.model.datasource.ListDataSource;
import com.example.fotij.userapplication.model.entities.TakeNGoConst;

public class LoginActivity extends AppCompatActivity {
    EditText username;
    EditText password;
    Button login;
    TextView register;
    BackEndFunc backEndFunc = FactoryMethod.getBackEndFunc(DataSourceType.DATA_INTERNET);
    String usernameclient;
    String passwordclient;
    CheckBox remembermebutton;
    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        remembermebutton = findViewById(R.id.remembermebutton);
        username = findViewById(R.id.UsernameeditText);
        password = findViewById(R.id.PasswordeditText);
        login = findViewById(R.id.Loginbutton);
        register = findViewById(R.id.RegistertextView);
        frameLayout=findViewById(R.id.loadingAccount);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if no view has focus:
                View view = LoginActivity.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                usernameclient = username.getText().toString();
                passwordclient = password.getText().toString();
                if (usernameclient == null || usernameclient.compareTo("") == 0) {
                    Toast.makeText(LoginActivity.this, "Please enter username", Toast.LENGTH_LONG).show();
                    return;
                }
                if (passwordclient == null || passwordclient.compareTo("") == 0) {
                    Toast.makeText(LoginActivity.this, "Please enter password", Toast.LENGTH_LONG).show();
                    return;
                }
                new ValidateAsync().execute();

            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    public class ValidateAsync extends AsyncTask<Void, Void, Void> {
        int validationid;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            frameLayout.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            validationid = backEndFunc.checkclient(usernameclient, passwordclient);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            switch (validationid) {
                case 0: {
                    frameLayout.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "Please enter the right password", Toast.LENGTH_LONG).show();
                    password.setText("");
                    return;
                }
                case -1: {
                    frameLayout.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "The username is incorrect", Toast.LENGTH_LONG).show();
                    username.setText("");
                    password.setText("");
                    return;
                }
                default: {
                    if (remembermebutton.isChecked())
                        saveSharedPreferences();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra(TakeNGoConst.ClientConst.ID, validationid);

                    finish();
                    startActivity(intent);
                    frameLayout.setVisibility(View.GONE);
                    return;
                }
            }
        }
    }

    private void saveSharedPreferences() {
        try {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(TakeNGoConst.Userconst.USERNAME, usernameclient);
            editor.putString(TakeNGoConst.Userconst.PASSWORD, passwordclient);
            editor.commit();
            Toast.makeText(this, "Username and Password has been saved", Toast.LENGTH_SHORT);
        } catch (Exception e) {
            Toast.makeText(this, "Failed to save Username and Password", Toast.LENGTH_SHORT);
        }
    }
}
