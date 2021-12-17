package com.cnit355.rr;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

public class FavouriteList extends AppCompatActivity {

    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_list);

        getSupportActionBar().setTitle("Favourite List");

        db=new DBHelper(this);

        if (!db.favouriteWords(Login.currentUser).isEmpty()){
            try{

                ArrayList <String> words = db.favouriteWords(Login.currentUser);
                ArrayList <String> explanations= db.favouriteWordExplanations(Login.currentUser);

                TextView list= findViewById(R.id.textViewList);

                String word_list="";

                for (int i = 0; i < words.size(); i++) {
                    // Printing and display the elements in ArrayList
                    word_list +=words.get(i).toUpperCase() + ": " + explanations.get(i)+ '\n' + '\n';
                }

                list.setText(word_list);

            }catch (Exception e){
            }

        }


    }
}