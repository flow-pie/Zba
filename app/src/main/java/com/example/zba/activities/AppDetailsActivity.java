package com.example.zba.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.zba.R;
import com.example.zba.data.PasswordRepository;
import com.example.zba.databinding.ActivityAppDetailsBinding;

public class AppDetailsActivity extends AppCompatActivity {

    private ActivityAppDetailsBinding appDetailsActivityBinding;
    private boolean isPasswordVisible = false;
    private boolean isEditing = false;


    private String id;
    private String appName;
    private String password;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appDetailsActivityBinding = ActivityAppDetailsBinding.inflate(getLayoutInflater());
        setContentView(appDetailsActivityBinding.getRoot());

//        recieve data from the intent
        appName = getIntent().getStringExtra("appName");
        username = getIntent().getStringExtra("username");
        password = getIntent().getStringExtra("password");
        id = getIntent().getStringExtra("password_id");

        if (appName == null || password == null) {
            Toast.makeText(this, "Error loading password details", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        setViewMode();

        //back btn
        appDetailsActivityBinding.topAppBar.setNavigationOnClickListener(v -> finish());
        appDetailsActivityBinding.btnTogglePass.setOnClickListener(v -> togglePasswordVisibility());
        appDetailsActivityBinding.btnCopyPassword.setOnClickListener(v -> copyPasswordToClipboard());


//        update or save changes
        appDetailsActivityBinding.btnUpdate.setOnClickListener(v -> {
            if (!isEditing)
                enterEditMode();
            else
                saveChanges();
        });

//        delete / cancel toogle
        appDetailsActivityBinding.btnDelete.setOnClickListener(v -> {
                    if (isEditing)
                        exitEditMode(false);
                    else {
                        PasswordRepository.removePassword(id);
                        Toast.makeText(this, "'Password deleted successfully", Toast.LENGTH_SHORT).show();
                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            // This code runs after the delay
                            finish();
                        }, 2000); // 2000 milliseconds = 2 seconds
                    }
                }
        );
    }

    // Mask password with bullets
    private String maskPassword(String password) {
        return "•".repeat(password.length());
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            appDetailsActivityBinding.tvPassword.setText(maskPassword(password));
        } else {
            appDetailsActivityBinding.tvPassword.setText(password);
        }
        isPasswordVisible = !isPasswordVisible;
    }

    // Copy password to clipboard
    private void copyPasswordToClipboard() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Password", password);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "Password copied to clipboard", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        appDetailsActivityBinding = null;
    }

    private void setViewMode() {
        //set data to views
        appDetailsActivityBinding.tvAppName.setText(appName);
        appDetailsActivityBinding.tvAppDisplayName.setText(appName);
        appDetailsActivityBinding.tvUsername.setText(username);
        appDetailsActivityBinding.tvPassword.setText(maskPassword(password));

        hideEditFields();
    }

    private void enterEditMode() {
        isEditing = true;

        //hide non editable views
        appDetailsActivityBinding.tvAppDisplayName.setVisibility(View.GONE);
        appDetailsActivityBinding.tvUsername.setVisibility(View.GONE);
        appDetailsActivityBinding.passwordView.setVisibility(View.GONE);
        appDetailsActivityBinding.appLbl.setVisibility(View.GONE);
        appDetailsActivityBinding.usernameLbl.setVisibility(View.GONE);
        appDetailsActivityBinding.passLbl.setVisibility(View.GONE);

        //show editable views
        appDetailsActivityBinding.editAppDisplayNameLayout.setVisibility(View.VISIBLE);
        appDetailsActivityBinding.editUsernameLayout.setVisibility(View.VISIBLE);
        appDetailsActivityBinding.editPasswordLayout.setVisibility(View.VISIBLE);

        //pre-fill the editable fields
        appDetailsActivityBinding.editAppDisplayName.setText(appName);
        appDetailsActivityBinding.editUsername.setText(username);
        appDetailsActivityBinding.editPassword.setText(password);

        appDetailsActivityBinding.btnUpdate.setText("Save");
        appDetailsActivityBinding.btnDelete.setText("Cancel");

        appDetailsActivityBinding.btnUpdate.setBackgroundTintList(
                ContextCompat.getColorStateList(this, R.color.colorGreen)
        );
        appDetailsActivityBinding.btnDelete.setBackgroundTintList(
                ContextCompat.getColorStateList(this, R.color.app_background)
        );
    }

    private void exitEditMode(boolean saved) {
        isEditing = false;

        hideEditFields();

        // Restore view mode visibility
        appDetailsActivityBinding.tvAppDisplayName.setVisibility(View.VISIBLE);
        appDetailsActivityBinding.tvUsername.setVisibility(View.VISIBLE);
        appDetailsActivityBinding.passwordView.setVisibility(View.VISIBLE);
        appDetailsActivityBinding.appLbl.setVisibility(View.VISIBLE);
        appDetailsActivityBinding.usernameLbl.setVisibility(View.VISIBLE);
        appDetailsActivityBinding.passLbl.setVisibility(View.VISIBLE);

        appDetailsActivityBinding.btnUpdate.setText("Update");
        appDetailsActivityBinding.btnDelete.setText("Delete");

        appDetailsActivityBinding.btnUpdate.setBackgroundTintList(
                ContextCompat.getColorStateList(this, R.color.black)
        );

        appDetailsActivityBinding.btnDelete.setBackgroundTintList(
                ContextCompat.getColorStateList(this, android.R.color.holo_red_light)
        );


        if (saved) setViewMode();
    }

    private void saveChanges() {
        String newAppName = appDetailsActivityBinding.editAppDisplayName.getText().toString().trim();
        String newUsername = appDetailsActivityBinding.editUsername.getText().toString().trim();
        String newPassword = appDetailsActivityBinding.editPassword.getText().toString().trim();

        if (newAppName.isEmpty() || newPassword.isEmpty()) {
            Toast.makeText(this, "App name and password cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update fields locally
        appName = newAppName;
        username = newUsername;
        password = newPassword;

        // Update in repository
        PasswordRepository.updatePassword(id, appName, username, password);

        Toast.makeText(this, "Details updated successfully!", Toast.LENGTH_SHORT).show();
        exitEditMode(true);
    }


    private void hideEditFields() {
        if (appDetailsActivityBinding.editAppDisplayNameLayout != null)
            appDetailsActivityBinding.editAppDisplayNameLayout.setVisibility(View.GONE);
        if (appDetailsActivityBinding.editUsernameLayout != null)
            appDetailsActivityBinding.editUsernameLayout.setVisibility(View.GONE);
        if (appDetailsActivityBinding.editPasswordLayout != null)
            appDetailsActivityBinding.editPasswordLayout.setVisibility(View.GONE);
    }
}

