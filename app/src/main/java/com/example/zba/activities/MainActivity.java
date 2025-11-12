package com.example.zba.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.zba.Adapter.PasswordAdapter;
import com.example.zba.R;
import com.example.zba.data.PasswordRepository;
import com.example.zba.databinding.ActivityAddPasswordBinding;
import com.example.zba.databinding.ActivityAppDetailsBinding;
import com.example.zba.databinding.ActivityMainBinding;
import com.example.zba.interfaces.OnPasswordClickListener;
import com.example.zba.models.PasswordItem;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements OnPasswordClickListener {

    private ActivityMainBinding mainActivityBinding;
    private List<PasswordItem> passwordItemList = PasswordRepository.getPasswords();

    private PasswordAdapter passwordAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivityBinding = ActivityMainBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);

        setContentView(mainActivityBinding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(mainActivityBinding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupRecyclerView();
        setupClickListeners();
        loadPasswords();
    }

//    always reload ui when resuming to main activity
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("PasswordRepoDebug", "---- Current Password List ----");
        for (PasswordItem item : PasswordRepository.getPasswords()) {
            Log.d("PasswordRepoDebug",
                    "ID: " + item.getId() +
                            " | App: " + item.getAppName() +
                            " | Username: " + item.getUserName() +
                            " | Password: " + item.getPassword());
        }
        Log.d("PasswordRepoDebug", "--------------------------------");
        passwordAdapter.notifyDataSetChanged();
        updateUI();
    }


    private void updateUI() {
        if(passwordItemList.isEmpty()){
            //empty state
            mainActivityBinding.emptyState.setVisibility(View.VISIBLE);
            mainActivityBinding.passwordListSection.setVisibility(View.GONE);
        }else{
            //pass list
            mainActivityBinding.emptyState.setVisibility(View.GONE);
            mainActivityBinding.passwordListSection.setVisibility(View.VISIBLE);

            passwordAdapter.notifyDataSetChanged();
        }
    }


    private void loadPasswords() {
        updateUI();

//       addSampleData();
    }


    private void openSearch() {
        Toast.makeText(this, "Work in progress. Search functionality coming soon!", Toast.LENGTH_SHORT).show();
    }
    private void openAddPassword() {
        Intent intent = new Intent(this, AddPasswordActivity.class);
        startActivity(intent);
    }

    private void setupClickListeners() {
        mainActivityBinding.btnAddFirst.setOnClickListener(v ->
                openAddPassword());
        mainActivityBinding.btnAddNew.setOnClickListener(v -> openAddPassword());

        mainActivityBinding.search.setOnClickListener(v-> openSearch());
    }

    private void setupRecyclerView() {
        mainActivityBinding.recyclerPasswords.setLayoutManager(new LinearLayoutManager(this));
        passwordAdapter = new PasswordAdapter(passwordItemList, this);
        mainActivityBinding.recyclerPasswords.setAdapter(passwordAdapter);
    }

    @Override
    public void onPasswordClick(PasswordItem item) {
        //open passwd details activity
        Intent intent = new Intent(this, AppDetailsActivity.class);
        intent.putExtra("appName", item.getAppName());
        intent.putExtra("username", item.getUserName());
        intent.putExtra("password", item.getPassword());
        intent.putExtra("password_id", item.getId());

        startActivity(intent);
    }


    @Override
    protected void onDestroy(){
        super.onDestroy();
        mainActivityBinding = null;
    }
}