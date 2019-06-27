package com.example.tangchemobile;

import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;

import com.example.tangchemobile.services.Scanner;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements OperationFragment.OnFragmentInteractionListener, SettingFragment.OnFragmentInteractionListener{

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.flContent, operationFragment).commit();
                    return true;
                case R.id.navigation_dashboard:
                    getSupportFragmentManager().beginTransaction().replace(R.id.flContent, settingFragment).commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        this.getSupportFragmentManager().beginTransaction().add(R.id.flContent,new OperationFragment()).commit();
        this.scanner = new Scanner(this.getApplicationContext());
        this.operationFragment = new OperationFragment();
        this.settingFragment = new SettingFragment();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void onFragmentInteraction(Uri uri) {
        Toast.makeText(this,"inter",Toast.LENGTH_LONG).show();
    }

    public Scanner GetScanner() {
        return this.scanner;
    }

    private Scanner scanner;


    private OperationFragment operationFragment;
    private SettingFragment settingFragment;
}
