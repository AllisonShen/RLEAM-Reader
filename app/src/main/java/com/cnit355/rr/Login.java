package com.cnit355.rr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    EditText user;
    EditText password;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user=findViewById(R.id.editTextUserName);
        password=findViewById(R.id.editTextPassword);
        db = new DBHelper(this); //create database object
    }

    public void onSignInClicked(View view) {

        String username = user.getText().toString();
        String passwd = password.getText().toString();

        //validating user details for sign in

        if(username.equals("")||passwd.equals(""))
            Toast.makeText(this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
        else{
            Boolean checkuserpass = db.checkusernamepassword(username, passwd);
            if(checkuserpass==true){
                Toast.makeText(this, "Sign in successful", Toast.LENGTH_SHORT).show();
                Intent intent  = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //go to register is user account doesnt exist
    public void onRegisterClicked(View view) {
        Intent intent= new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}
