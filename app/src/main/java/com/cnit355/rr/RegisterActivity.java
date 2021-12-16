package com.cnit355.rr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    EditText user;
    EditText password;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        user=findViewById(R.id.editTextUserName);
        password=findViewById(R.id.editTextPassword);
        db = new DBHelper(this); //create database

    }

    public void onRegisterClicked(View view) {

        String username= user.getText().toString();
        String passwd= password.getText().toString();

        if(username.equals("")||passwd.equals("")) // if user or password empty
            Toast.makeText(this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
        else{
            Boolean checkuser = db.checkusername(username); //verifying user is unique
            if(checkuser==false){
                Boolean insert = db.insertData(username, passwd); //insert data
                if(insert==true){
                    Toast.makeText(this, "Registered successfully.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),Login.class); //go to login after registering
                    startActivity(intent);
                }else{
                    Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(this, "User already exists! Please sign in", Toast.LENGTH_SHORT).show();
            }

        }
    }

}


