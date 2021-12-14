package com.cnit355.rr;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.cnit355.rr.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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