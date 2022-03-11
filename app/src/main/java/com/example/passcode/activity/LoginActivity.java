package com.example.passcode.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.Toast;

import com.example.passcode.KeyboardView;
import com.example.passcode.R;
import com.example.passcode.databinding.ActivityLoginBinding;
import com.example.passcode.databinding.ActivityMainBinding;

import java.util.concurrent.Executor;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding activityLoginBinding;
    KeyboardView keyboardView;
    private SharedPreferences sharedPreferences;
    BiometricManager biometricManager;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    private Executor executor;
    private int attempt = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        activityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        sharedPreferences = getSharedPreferences("SecretCode", Context.MODE_PRIVATE);
        keyboardView = new KeyboardView(getApplicationContext());
        keyboardView.init();
        keyboardView.getActivity(this);
        KeyboardView keyboard = (KeyboardView) findViewById(R.id.myKeyboard);
        // prevent system keyboard from appearing when EditText is tapped
        activityLoginBinding.editText.setRawInputType(InputType.TYPE_CLASS_TEXT);
        activityLoginBinding.frameLayoutDialPad.setVisibility(View.GONE);
        InputConnection ic = activityLoginBinding.editText.onCreateInputConnection(new EditorInfo());
        keyboard.setInputConnection(ic);

        BiometricManager biometricManager = BiometricManager.from(getApplicationContext());
        switch (biometricManager.canAuthenticate()) {
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Toast.makeText(getApplicationContext(), "Device does not have Fingerprint", Toast.LENGTH_SHORT).show();
                break;

            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Toast.makeText(getApplicationContext(), "Not working", Toast.LENGTH_SHORT).show();
                break;

            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Toast.makeText(getApplicationContext(), "No finger print assigned", Toast.LENGTH_SHORT).show();
                break;

            case BiometricManager.BIOMETRIC_STATUS_UNKNOWN:
                Toast.makeText(getApplicationContext(), "Unknown person", Toast.LENGTH_SHORT).show();
                break;
        }
        executor = ContextCompat.getMainExecutor(getApplicationContext());
        biometricPrompt = new BiometricPrompt(LoginActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Log.e("Fingerprint ==>", errString.toString());
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });

        activityLoginBinding.editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityLoginBinding.frameLayoutDialPad.setVisibility(View.VISIBLE);
            }
        });

        activityLoginBinding.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                String pin = activityLoginBinding.editText.getText().toString();
//                String sPin = sharedPreferences.getString("pin", "");
//                if (pin.length() > 4) {
//                    if (!pin.equals(sPin)) {
//                        Toast.makeText(LoginActivity.this, "Wrong pin", Toast.LENGTH_SHORT).show();
//                    }
//                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String pin = activityLoginBinding.editText.getText().toString();
                String sPin = sharedPreferences.getString("pin", "");
                if (pin.equals(sPin)) {
                    Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    activityLoginBinding.editText.setText("");
                    activityLoginBinding.frameLayoutDialPad.setVisibility(View.GONE);
                } else if (pin.length() == 4) {
                    if (attempt < 4) {
                        if (!pin.equals(sPin)) {
                            Toast.makeText(LoginActivity.this, "Wrong pin", Toast.LENGTH_SHORT).show();
                        }
                    } else if (attempt == 4) {
                        Toast.makeText(LoginActivity.this, "Login limit exceed", Toast.LENGTH_SHORT).show();
                    } else if (attempt > 4) {
                        System.exit(0);
                    }
                    attempt++;
                }

            }
        });

        activityLoginBinding.constrainLayoutOuter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityLoginBinding.frameLayoutDialPad.setVisibility(View.GONE);
            }
        });

        activityLoginBinding.imageViewFingerPrint.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View view) {
                try {

                    promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("Test app")
                            .setDescription("Use your fingerprint to unlock")
                            .setDeviceCredentialAllowed(true)
                            .build();

                    biometricPrompt.authenticate(promptInfo);
                } catch (Exception e) {
                    Log.e("Error==> ", e.getMessage());
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}