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
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String pin = activityGeneratePasscodeBinding.editTextPin.getText().toString();
                    String confirmPin = activityGeneratePasscodeBinding.editTextConfirmPin.getText().toString();
                    if (confirmPin.equals(pin)) {
                        activityGeneratePasscodeBinding.mTextViewConfirmPinError.setVisibility(View.VISIBLE);
                        activityGeneratePasscodeBinding.mTextViewConfirmPinError.setText("Matched");
                    } else {
                        activityGeneratePasscodeBinding.mTextViewConfirmPinError.setVisibility(View.VISIBLE);
                        activityGeneratePasscodeBinding.mTextViewConfirmPinError.setText("Not Match");
                    }
                }
            });

            activityGeneratePasscodeBinding.buttonGenerate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String pin = activityGeneratePasscodeBinding.editTextPin.getText().toString();
                    String confirmPin = activityGeneratePasscodeBinding.editTextConfirmPin.getText().toString();
                    String hint = activityGeneratePasscodeBinding.editTextHint.getText().toString();

                    if (pin.equals("")) {
                        activityGeneratePasscodeBinding.mTextViewPinError.setVisibility(View.VISIBLE);
                        activityGeneratePasscodeBinding.mTextViewPinError.setText("Please Enter the Pin");
                    } else {
                        activityGeneratePasscodeBinding.mTextViewPinError.setVisibility(View.GONE);
                    }
                    if (pin.length() < 4) {
                        activityGeneratePasscodeBinding.mTextViewPinError.setVisibility(View.VISIBLE);
                        activityGeneratePasscodeBinding.mTextViewPinError.setText("Enter 4 digit pin");
                    } else {
                        activityGeneratePasscodeBinding.mTextViewPinError.setVisibility(View.GONE);
                    }
                    if (confirmPin.equals("")) {
                        activityGeneratePasscodeBinding.mTextViewConfirmPinError.setVisibility(View.VISIBLE);
                        activityGeneratePasscodeBinding.mTextViewConfirmPinError.setText("Please Enter the confirm pin");
                    } else {
                        activityGeneratePasscodeBinding.mTextViewConfirmPinError.setVisibility(View.GONE);
                    }

                    if (hint.equals("")) {
                        activityGeneratePasscodeBinding.mTextViewHintError.setVisibility(View.VISIBLE);
                        activityGeneratePasscodeBinding.mTextViewHintError.setText("Please Enter the hint");
                    } else {
                        activityGeneratePasscodeBinding.mTextViewHintError.setVisibility(View.GONE);
                    }

                    if (!pin.equals("") && !confirmPin.equals("") && !hint.equals("") && pin.length() == 4) {
                        if (pin.equals(confirmPin) && pin.length() == confirmPin.length()) {
                            activityGeneratePasscodeBinding.mTextViewPinError.setVisibility(View.GONE);
                            activityGeneratePasscodeBinding.mTextViewConfirmPinError.setVisibility(View.GONE);
                            activityGeneratePasscodeBinding.mTextViewHintError.setVisibility(View.GONE);
                            editor.putString("pin", confirmPin);
                            editor.putString("hint", hint);
                            editor.putString("status", "Generated");
                            editor.apply();
                            Toast.makeText(getApplicationContext(), "Generated", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(GeneratePasscodeActivity.this, LoginActivity.class);
                            startActivity(intent);
                            clearText();
                        } else {
                            activityGeneratePasscodeBinding.mTextViewConfirmPinError.setVisibility(View.VISIBLE);
                            activityGeneratePasscodeBinding.mTextViewConfirmPinError.setText("Not matching");
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

    private void clearText() {
        activityGeneratePasscodeBinding.mTextViewPinError.setVisibility(View.GONE);
        activityGeneratePasscodeBinding.mTextViewConfirmPinError.setVisibility(View.GONE);
        activityGeneratePasscodeBinding.mTextViewHintError.setVisibility(View.GONE);
        activityGeneratePasscodeBinding.editTextPin.setText("");
        activityGeneratePasscodeBinding.editTextConfirmPin.setText("");
        activityGeneratePasscodeBinding.editTextHint.setText("");
    }
}