package com.cnit355.rr;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
//code for helper class/login/register: https://www.learnandroid.net/2020/05/login-and-register-activity-using.html
//Class to handle database functions

public class DBHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "UserLogin.db"; //name database

    public DBHelper(Context context) {
        super(context, "UserLogin.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("create Table users(username TEXT primary key, password TEXT)"); //Create table users with primary key username and password field
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {
        DB.execSQL("drop Table if exists users");
    }

    public Boolean insertData(String username, String password){
        SQLiteDatabase LoginDB = this.getWritableDatabase();
        ContentValues contentValues= new ContentValues(); //content values for key-value pair of username and password

        //insert username and password into table
        contentValues.put("username", username);
        contentValues.put("password", password);
        long result = LoginDB.insert("users", null, contentValues);
        if(result==-1) return false;
        else
            return true;
    }

    public Boolean checkusername(String username) {

        //check if user is in table using cursor to go through rows
        SQLiteDatabase LoginDB = this.getWritableDatabase();
        Cursor cursor = LoginDB.rawQuery("Select * from users where username = ?", new String[]{username});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    public Boolean checkusernamepassword(String username, String password){
        SQLiteDatabase LoginDB = this.getWritableDatabase();

        //check password of user in table using cursor to go through rows
        Cursor cursor = LoginDB.rawQuery("Select * from users where username = ? and password = ?", new String[] {username,password});
        if(cursor.getCount()>0)
            return true;
        else
            return false;
    }
}
