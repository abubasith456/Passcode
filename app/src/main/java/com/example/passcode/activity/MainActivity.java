package com.example.passcode.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.passcode.R;
import com.example.passcode.databinding.ActivityLoginBinding;
import com.example.passcode.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding activityMainBinding;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        sharedPreferences = getSharedPreferences("SecretCode", Context.MODE_PRIVATE);
        status = sharedPreferences.getString("status", "");
        editor = sharedPreferences.edit();

        checkStatus();

        try {
            activityMainBinding.editTextConfirmPin.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String pin = activityMainBinding.editTextPin.getText().toString();
                    String confirmPin = activityMainBinding.editTextConfirmPin.getText().toString();
                    if (confirmPin.equals(pin)) {
                        activityMainBinding.mTextViewConfirmPinError.setVisibility(View.VISIBLE);
                        activityMainBinding.mTextViewConfirmPinError.setText("Matched");
                    } else {
                        activityMainBinding.mTextViewConfirmPinError.setVisibility(View.VISIBLE);
                        activityMainBinding.mTextViewConfirmPinError.setText("Not Match");
                    }
                }
            });

            activityMainBinding.buttonGenerate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String pin = activityMainBinding.editTextPin.getText().toString();
                    String confirmPin = activityMainBinding.editTextConfirmPin.getText().toString();
                    String hint = activityMainBinding.editTextHint.getText().toString();

                    if (pin.equals("")) {
                        activityMainBinding.mTextViewPinError.setVisibility(View.VISIBLE);
                        activityMainBinding.mTextViewPinError.setText("Please Enter the Pin");
                    } else {
                        activityMainBinding.mTextViewPinError.setVisibility(View.GONE);
                    }
                    if (pin.length() < 4) {
                        activityMainBinding.mTextViewPinError.setVisibility(View.VISIBLE);
                        activityMainBinding.mTextViewPinError.setText("Enter 4 digit pin");
                    } else {
                        activityMainBinding.mTextViewPinError.setVisibility(View.GONE);
                    }
                    if (confirmPin.equals("")) {
                        activityMainBinding.mTextViewConfirmPinError.setVisibility(View.VISIBLE);
                        activityMainBinding.mTextViewConfirmPinError.setText("Please Enter the confirm pin");
                    } else {
                        activityMainBinding.mTextViewConfirmPinError.setVisibility(View.GONE);
                    }

                    if (hint.equals("")) {
                        activityMainBinding.mTextViewHintError.setVisibility(View.VISIBLE);
                        activityMainBinding.mTextViewHintError.setText("Please Enter the hint");
                    } else {
                        activityMainBinding.mTextViewHintError.setVisibility(View.GONE);
                    }

                    if (!pin.equals("") && !confirmPin.equals("") && !hint.equals("") && pin.length() == 4) {
                        if (pin.equals(confirmPin) && pin.length() == confirmPin.length()) {
                            activityMainBinding.mTextViewPinError.setVisibility(View.GONE);
                            activityMainBinding.mTextViewConfirmPinError.setVisibility(View.GONE);
                            activityMainBinding.mTextViewHintError.setVisibility(View.GONE);
                            editor.putString("pin", confirmPin);
                            editor.putString("hint", hint);
                            editor.putString("status", "Generated");
                            editor.apply();
                            Toast.makeText(getApplicationContext(), "Generated", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                            clearText();
                        } else {
                            activityMainBinding.mTextViewConfirmPinError.setVisibility(View.VISIBLE);
                            activityMainBinding.mTextViewConfirmPinError.setText("Not matching");
                        }

                    }
//                    String check = sharedPreferences.getString("pin", "");
//                    Toast.makeText(getContext(), "" + check, Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            Log.e("Error==>", e.getMessage());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkStatus();

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkStatus();
    }

    private void checkStatus() {
        if (status.equals("Generated")) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    private void clearText() {
        activityMainBinding.mTextViewPinError.setVisibility(View.GONE);
        activityMainBinding.mTextViewConfirmPinError.setVisibility(View.GONE);
        activityMainBinding.mTextViewHintError.setVisibility(View.GONE);
        activityMainBinding.mTextViewPinError.setText("");
        activityMainBinding.mTextViewConfirmPinError.setText("");
        activityMainBinding.mTextViewHintError.setText("");
        activityMainBinding.editTextPin.setText("");
        activityMainBinding.editTextConfirmPin.setText("");
        activityMainBinding.editTextHint.setText("");
    }

}