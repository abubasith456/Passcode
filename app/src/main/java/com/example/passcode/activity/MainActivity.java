package com.example.passcode.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.passcode.R;
import com.example.passcode.databinding.ActivityLoginBinding;

public class MainActivity extends AppCompatActivity {

    ActivityLoginBinding activityLoginBinding;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
        activityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        sharedPreferences = getSharedPreferences("SecretCode", Context.MODE_PRIVATE);
//        editor = sharedPreferences.edit();
        String status = sharedPreferences.getString("status", "");


        if (status.equals("Generated")) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(MainActivity.this, GeneratePasscodeActivity.class);
            startActivity(intent);
        }
//            activityLoginBinding.buttonPasscodeGenerate.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    try {
//
//                    } catch (Exception e) {
//                        Log.e("Error==>", e.getMessage());
//                    }
//                }
//            });
    }

    private void showPasscodeLoginPage() {
        try {

        } catch (Exception e) {
            Log.e("Error==>", e.getMessage());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}