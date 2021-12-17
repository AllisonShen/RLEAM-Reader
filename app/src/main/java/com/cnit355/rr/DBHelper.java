package com.cnit355.rr;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

//code for helper class/login/register: https://www.learnandroid.net/2020/05/login-and-register-activity-using.html
//code for cursor querying: https://www.geeksforgeeks.org/how-to-read-data-from-sqlite-database-in-android/
//Class to handle database functions

public class DBHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "UserLogin.db"; //name database

    public DBHelper(Context context) {
        super(context, "UserLogin.db", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("create Table if not exists users(username TEXT primary key, password TEXT)"); //Create table users with primary key username and password field
        DB.execSQL("create Table if not exists favourites(username TEXT, word TEXT, explanation TEXT, FOREIGN KEY (username)\n" +
                "    REFERENCES users (username), primary key (username, word) )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {
        DB.execSQL("drop Table if exists users");
        DB.execSQL("drop Table if exists favourites");
        onCreate(DB);
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
        if (cursor.getCount() > 0){
            cursor.close();
            return true;
        }
        else{
            cursor.close();
            return false;}

    }

    public Boolean checkusernamepassword(String username, String password){
        SQLiteDatabase LoginDB = this.getWritableDatabase();

        //check password of user in table using cursor to go through rows
        Cursor cursor = LoginDB.rawQuery("Select * from users where username = ? and password = ? ", new String[] {username,password});

        if(cursor.getCount()>0)
        {
            cursor.close();
            return true;
        }
        else{
            cursor.close();
            return false;
        }


    }

    //adding a user's favourite words to the database

    public boolean insertFavouriteWord(String username, String word, String explanation) {

        SQLiteDatabase LoginDB = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put("username", username);
        values.put("word", word);
        values.put("explanation", explanation);

        // after adding all values we are passing
        // content values to our table.
        long result= LoginDB.insert("favourites", null, values);

        if(result==-1) return false;
        else
            return true;

    }

    //to use: create  DBHelper db = new DBHelper(this); as global variable
    // Boolean insertWord = db.insertFavouriteWord(Login.currentUser, word);
    //            if(insertWord==true){
    //                Toast.makeText(this, "Favourite word saved!", Toast.LENGTH_SHORT).show();
    //            }else{
    //                Toast.makeText(this, "Could not save favourite word", Toast.LENGTH_SHORT).show();
    //            }


    //method to see a users favourite words, returns array of the words
    public ArrayList<String> favouriteWords(String username) {

        SQLiteDatabase LoginDB = this.getReadableDatabase();

        // on below line we are creating a cursor with query to read data from database.
        Cursor cursor = LoginDB.rawQuery("Select word from favourites where username = ? ", new String[] {username});

        // on below line we are creating a new array list.
        ArrayList<String> favouriteWords = new ArrayList<>();

        // moving our cursor to first position.
        if (cursor.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.
                favouriteWords.add((cursor.getString(cursor.getColumnIndexOrThrow("word"))));

            } while (cursor.moveToNext());
            // moving our cursor to next.
        }
        // at last closing our cursor
        // and returning our array list.
        cursor.close();
        return favouriteWords;
    }

    public ArrayList<String> favouriteWordExplanations(String username) {

        SQLiteDatabase LoginDB = this.getReadableDatabase();

        // on below line we are creating a cursor with query to read data from database.
        Cursor cursor = LoginDB.rawQuery("Select explanation from favourites where username = ? ", new String[] {username});

        // on below line we are creating a new array list.
        ArrayList<String> favouriteWordExplanation = new ArrayList<>();

        // moving our cursor to first position.
        if (cursor.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.
                favouriteWordExplanation.add((cursor.getString(cursor.getColumnIndexOrThrow("explanation"))));

            } while (cursor.moveToNext());
            // moving our cursor to next.
        }
        // at last closing our cursor
        // and returning our array list.
        cursor.close();
        return favouriteWordExplanation;
    }

    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }


}
