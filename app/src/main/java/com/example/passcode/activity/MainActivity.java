package com.example.passcode.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.passcode.R;
import com.example.passcode.databinding.ActivityLoginBinding;
import com.example.passcode.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding activityMainBinding;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        sharedPreferences = getSharedPreferences("SecretCode", Context.MODE_PRIVATE);
        String status = sharedPreferences.getString("status", "");

        if (status.equals("Generated")) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(MainActivity.this, GeneratePasscodeActivity.class);
            startActivity(intent);
        }

    }

}