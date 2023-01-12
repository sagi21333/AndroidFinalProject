package com.example.androidfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.androidfinalproject.model.Model;

public class MainActivity extends AppCompatActivity {
    NavController navCtrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavHost navHost = (NavHost) getSupportFragmentManager().findFragmentById(R.id.mainNavHost);
        navCtrl = navHost.getNavController();
        NavigationUI.setupActionBarWithNavController(this, navCtrl);

        if (Model.instance.isSignedIn()) {
            navCtrl.navigate(R.id.action_loginFragment_to_moviesFragment2);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (!super.onOptionsItemSelected(item)) {
            switch (item.getItemId()) {
                case android.R.id.home:
                    navCtrl.navigateUp();
                    break;
                default:
                    NavigationUI.onNavDestinationSelected(item,navCtrl);
            }
        } else {
            return true;
        }
        return false;
    }
}