package com.example.fotij.userapplication.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.fotij.userapplication.model.backend.BackEndFunc;
import com.example.fotij.userapplication.model.backend.DataSourceType;
import com.example.fotij.userapplication.model.backend.FactoryMethod;
import com.example.fotij.userapplication.model.entities.TakeNGoConst;


/**
 * Created by shmuel on 28/11/2017.
 */

public class SplashActivity extends AppCompatActivity {
    BackEndFunc backEndFunc = FactoryMethod.getBackEndFunc(DataSourceType.DATA_INTERNET);
    String usernameclient;
    String passwordclient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadSharedPreferences();
    }

    public class ValidateAsync extends AsyncTask<Void, Void, Void> {
        int validationid;

        @Override
        protected Void doInBackground(Void... voids) {
            validationid = backEndFunc.checkclient(usernameclient, passwordclient);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            intent.putExtra(TakeNGoConst.ClientConst.ID, validationid);
            startActivity(intent);
            return;
        }

    }


    private void loadSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPreferences.contains(TakeNGoConst.Userconst.USERNAME) && sharedPreferences.contains(TakeNGoConst.Userconst.PASSWORD)) {
            usernameclient = sharedPreferences.getString(TakeNGoConst.Userconst.PASSWORD, "");
            passwordclient = sharedPreferences.getString(TakeNGoConst.Userconst.USERNAME, "");
            new ValidateAsync().execute();
        } else {
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

}
