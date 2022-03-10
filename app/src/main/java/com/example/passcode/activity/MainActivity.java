package com.example.passcode.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

import com.example.passcode.KeyboardView;
import com.example.passcode.R;
import com.example.passcode.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding activityMainBinding;
    KeyboardView keyboardView;
    InputConnection inputConnection;
    String password = "4321";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        sharedPreferences = getSharedPreferences("SecretCode", Context.MODE_PRIVATE);
        keyboardView = new KeyboardView(getApplicationContext());
        keyboardView.init();
        keyboardView.getActivity(this);

        KeyboardView keyboard = (KeyboardView) findViewById(R.id.myKeyboard);
        // prevent system keyboard from appearing when EditText is tapped
        activityMainBinding.editText.setRawInputType(InputType.TYPE_CLASS_TEXT);
        activityMainBinding.frameLayoutDialPad.setVisibility(View.GONE);
        InputConnection ic = activityMainBinding.editText.onCreateInputConnection(new EditorInfo());
        keyboard.setInputConnection(ic);

        activityMainBinding.editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityMainBinding.frameLayoutDialPad.setVisibility(View.VISIBLE);
            }
        });

        activityMainBinding.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void afterTextChanged(Editable editable) {
                String pin = activityMainBinding.editText.getText().toString();
                String sPin = sharedPreferences.getString("pin", "");
                if (pin.equals(sPin)) {
                    Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                    startActivity(intent);
                }
            }
        });

        activityMainBinding.constrainLayoutOuter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityMainBinding.frameLayoutDialPad.setVisibility(View.GONE);
            }
        });
    }
}