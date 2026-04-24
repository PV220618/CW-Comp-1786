package com.example.expensetracker.activities;

import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.expensetracker.R;
import com.example.expensetracker.network.SyncRepository;
import com.example.expensetracker.utils.NetworkUtils;

public class SyncActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

        Button btnUploadAll = findViewById(R.id.btnUploadAll);
        TextView tvSyncResult = findViewById(R.id.tvSyncResult);

        btnUploadAll.setOnClickListener(v -> {
            if (!NetworkUtils.isInternetAvailable(this)) {
                Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
                return;
            }
            String result = new SyncRepository(this).uploadAll();
            tvSyncResult.setText(result);
        });
    }
}