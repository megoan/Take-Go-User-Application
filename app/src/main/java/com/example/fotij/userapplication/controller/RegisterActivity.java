package com.example.fotij.userapplication.controller;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fotij.userapplication.R;
import com.example.fotij.userapplication.model.backend.BackEndFunc;
import com.example.fotij.userapplication.model.backend.DataSourceType;
import com.example.fotij.userapplication.model.backend.FactoryMethod;
import com.example.fotij.userapplication.model.backend.Updates;
import com.example.fotij.userapplication.model.entities.Client;
import com.example.fotij.userapplication.model.entities.TakeNGoConst;
import com.example.fotij.userapplication.model.entities.User;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private EditText name;
    private EditText lastname;
    private EditText id;
    private EditText phone;
    private EditText Email;
    private EditText credit;
    private Button register;
    private Client client = new Client();
    private User user = new User();
    BackEndFunc backEndFunc = FactoryMethod.getBackEndFunc(DataSourceType.DATA_INTERNET);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findViews();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameclient = username.getText().toString();
                String passwordclient = password.getText().toString();
                String nameclient = name.getText().toString();
                String lastnameclient = lastname.getText().toString();
                String idclient = id.getText().toString();
                String phoneclient = phone.getText().toString();
                String emailclient = Email.getText().toString();
                String creditclient = credit.getText().toString();
                if (usernameclient == null || usernameclient.compareTo("") == 0 ||
                        passwordclient == null || passwordclient.compareTo("") == 0 ||
                        nameclient == null || nameclient.compareTo("") == 0 ||
                        lastnameclient == null || lastnameclient.compareTo("") == 0 ||
                        idclient == null || idclient.compareTo("") == 0 ||
                        phoneclient == null || phoneclient.compareTo("") == 0 ||
                        emailclient == null || emailclient.compareTo("") == 0 ||
                        creditclient == null || creditclient.compareTo("") == 0
                        ) {
                    Toast.makeText(RegisterActivity.this, "Please fill all fields", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!emailValidator(emailclient)) {
                    Toast.makeText(RegisterActivity.this, "Please enter correct email", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!usernameclient.matches(("[a-zA-Z0-9]*"))) {
                    Toast.makeText(RegisterActivity.this, "Please enter only letters and numbers for username", Toast.LENGTH_LONG).show();
                    return;
                }
                user.setPassword(passwordclient);
                user.setUsername(usernameclient);
                client.setUsername(usernameclient);
                client.setName(nameclient);
                client.setLastName(lastnameclient);
                client.setId(Integer.parseInt(idclient));
                client.setPhoneNum(phoneclient);
                client.setEmailAddress(emailclient);
                client.setCreditCardNum(creditclient);
                new ValidationClient().execute();
            }
        });
    }


    private void findViews() {
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        name = (EditText) findViewById(R.id.name);
        lastname = (EditText) findViewById(R.id.lastname);
        id = (EditText) findViewById(R.id.id);
        phone = (EditText) findViewById(R.id.phone);
        Email = (EditText) findViewById(R.id.Email);
        credit = (EditText) findViewById(R.id.credit);
        register = (Button) findViewById(R.id.register);
    }

    public boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public class ValidationClient extends AsyncTask<Void, Void, Void> {
        Updates validationuser;
        Updates validationclient;

        @Override
        protected Void doInBackground(Void... voids) {
            validationuser = backEndFunc.addUser(user);
            validationclient = backEndFunc.addClient(client);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            switch (validationuser) {
                case DUPLICATE: {
                    Toast.makeText(RegisterActivity.this, "This username is already taken ", Toast.LENGTH_LONG).show();
                    username.setText("");
                    return;
                }
            }
            switch (validationclient) {
                case DUPLICATE: {
                    Toast.makeText(RegisterActivity.this, "Please enter your real id", Toast.LENGTH_LONG).show();
                    id.setText("");
                    return;
                }
            }

            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            intent.putExtra(TakeNGoConst.ClientConst.ID, client.getId());
            startActivity(intent);
            return;

        }

    }
}

