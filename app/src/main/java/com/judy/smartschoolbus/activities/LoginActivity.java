package com.judy.smartschoolbus.activities;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.MapView;
import com.judy.smartschoolbus.R;

import Adapters.loginTabAdapter;

public class LoginActivity extends AppCompatActivity {

     FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mFragmentManager = getSupportFragmentManager();
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0358B2")));

        mFragmentTransaction = mFragmentManager.beginTransaction();

        mFragmentTransaction.replace(R.id.activity_main,new loginTabAdapter()).commit();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MapView mv = new MapView(getApplicationContext());
                    mv.onCreate(null);
                    mv.onPause();
                    mv.onDestroy();
                }catch (Exception ignored){

                }
            }
        }).start();


    }

}
