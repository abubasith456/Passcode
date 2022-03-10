package com.example.passcode.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.passcode.R;
import com.example.passcode.databinding.ActivityGeneratePasscodeBinding;

public class GeneratePasscodeActivity extends AppCompatActivity {

    ActivityGeneratePasscodeBinding activityGeneratePasscodeBinding;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_generate_passcode);
        activityGeneratePasscodeBinding = DataBindingUtil.setContentView(this, R.layout.activity_generate_passcode);
        sharedPreferences = getSharedPreferences("SecretCode", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();


        try {
            activityGeneratePasscodeBinding.editTextConfirmPin.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String pin = activityGeneratePasscodeBinding.editTextPin.getText().toString();
                    if (charSequence.equals(pin)) {
                        activityGeneratePasscodeBinding.editTextConfirmPinLayout.setHelperText("Match");
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String pin = activityGeneratePasscodeBinding.editTextPin.getText().toString();
                    String confirmPin = activityGeneratePasscodeBinding.editTextConfirmPin.getText().toString();
                    if (confirmPin.equals(pin)) {
                        activityGeneratePasscodeBinding.editTextConfirmPinLayout.setHelperText("Matched");
                    } else {
                        activityGeneratePasscodeBinding.editTextConfirmPinLayout.setHelperText("Not match");

                    }
                }
            });

            activityGeneratePasscodeBinding.buttonGenerate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String pin = activityGeneratePasscodeBinding.editTextPin.getText().toString();
                    String confirmPin = activityGeneratePasscodeBinding.editTextConfirmPin.getText().toString();
                    String hint = activityGeneratePasscodeBinding.editTextHint.getText().toString();
                    if (!pin.equals("") && !confirmPin.equals("") && !hint.equals("")) {
                        editor.putString("pin", confirmPin);
                        editor.putString("hint", hint);
                        editor.putString("status", "Generated");
                        editor.apply();
                        Toast.makeText(getApplicationContext(), "Generated", Toast.LENGTH_SHORT).show();
                    }
//                    String check = sharedPreferences.getString("pin", "");
//                    Toast.makeText(getContext(), "" + check, Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            Log.e("Error==>", e.getMessage());
        }

    }
}