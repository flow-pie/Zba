package com.example.zba.activities;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.zba.data.PasswordRepository;
import com.example.zba.databinding.ActivityAddPasswordBinding;
import com.example.zba.models.PasswordItem;
import com.example.zba.utils.PasswordGenerator;

import java.util.UUID;

public class AddPasswordActivity extends AppCompatActivity {

    private ActivityAddPasswordBinding addPasswdActivityBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPasswdActivityBinding = ActivityAddPasswordBinding.inflate(getLayoutInflater());
        setContentView(addPasswdActivityBinding.getRoot());

        // Generate strong password button click
        addPasswdActivityBinding.btnSecure.setOnClickListener(v -> {
            String generatedPassword = PasswordGenerator.generateStrongPassword(16);
            addPasswdActivityBinding.addAppPassword.setText(generatedPassword);
            updatePasswordStrength(generatedPassword);
        });

        // update strength
        addPasswdActivityBinding.addAppPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updatePasswordStrength(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        addPasswdActivityBinding.btnSave.setOnClickListener(v -> savePassword());

        addPasswdActivityBinding.btnClose.setOnClickListener(v -> finish());

        addPasswdActivityBinding.topAppBar.setOnClickListener(v -> finish());
    }

    //password strength evaluator
    private void updatePasswordStrength(String password) {
        int strength = calculateStrength(password);
        addPasswdActivityBinding.passwordStrengthBar.setProgress(strength);

        if(strength < 40){
            addPasswdActivityBinding.tvStrengthStatus.setText("Weak");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                addPasswdActivityBinding.tvStrengthStatus.setTextColor(getColor(android.R.color.holo_orange_light));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                addPasswdActivityBinding.passwordStrengthBar.setProgressTintList(getColorStateList(android.R.color.holo_orange_light));
            }
        } else if(strength < 70){
            addPasswdActivityBinding.tvStrengthStatus.setText("Medium");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                addPasswdActivityBinding.tvStrengthStatus.setTextColor(getColor(android.R.color.holo_red_light));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                addPasswdActivityBinding.passwordStrengthBar.setProgressTintList(getColorStateList(android.R.color.holo_red_light));
            }
        } else {
            addPasswdActivityBinding.tvStrengthStatus.setText("Strong");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                addPasswdActivityBinding.tvStrengthStatus.setTextColor(getColor(android.R.color.holo_green_dark));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                addPasswdActivityBinding.passwordStrengthBar.setProgressTintList(getColorStateList(android.R.color.holo_green_dark));
            }
        }
    }

    // scoring algorithm
    private int calculateStrength(String password) {
        int score = 0;
        if(password.length() >= 8) score += 30;
        if(password.matches(".*[A-Z].*")) score += 20;
        if(password.matches(".*[0-9].*")) score += 20;
        if(password.matches(".*[@#$%^&+=!].*")) score += 30;
        return Math.min(score, 100);
    }

    private void savePassword() {
        String appName = addPasswdActivityBinding.addAppName.getText().toString().trim();
        String username = addPasswdActivityBinding.addAppUsername.getText().toString().trim();
        String password = addPasswdActivityBinding.addAppPassword.getText().toString();

        if(appName.isEmpty() || username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        PasswordItem newItem = new PasswordItem(UUID.randomUUID().toString(),appName, username, password);
        PasswordRepository.addPassword(newItem);
        Toast.makeText(this, "Password saved for " + appName, Toast.LENGTH_SHORT).show();

        // Finish activity and return to main
        finish();
    }
}
