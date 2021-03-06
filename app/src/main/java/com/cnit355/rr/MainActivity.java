package com.cnit355.rr;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.cnit355.rr.databinding.ActivityMainBinding;

//code for menu items: https://www.journaldev.com/9357/android-actionbar-example-tutorial

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db= new DBHelper(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case R.id.navigation_favList:
                Intent favList = new Intent(this, FavouriteList.class);
                startActivity(favList);
                return true;
            case R.id.navigation_forgetCurve:
                Toast.makeText(this, "Selected forgetting curve", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.navigation_signout:
                Intent mIntent = new Intent(this, Login.class);
                startActivity(mIntent);
                db.closeDB(); //close database
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void viewDetailsPDF(View view){
        Intent mIntent = new Intent(this, ReadViewEPUB.class);
        startActivity(mIntent);

    }
    public void viewDetailsEPUB(View view){
        Intent mIntent = new Intent(this, ReadViewEPUB.class);
        startActivity(mIntent);
    }
    public void convertButton(View view){ // pdf to epub
        Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://cloudconvert.com/pdf-to-epub"));
        startActivity(mIntent);
    }
    //https://cloudconvert.com/epub-to-pdf
    public void convertButtonToPDF(View view){ // pdf to epub
        Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://cloudconvert.com/epub-to-pdf"));
        startActivity(mIntent);
    }



}