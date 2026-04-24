package com.example.expensetracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.expensetracker.R;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnProjects = findViewById(R.id.btnProjects);
        Button btnSearch = findViewById(R.id.btnSearch);
        Button btnSync = findViewById(R.id.btnSync);

        btnProjects.setOnClickListener(v -> startActivity(new Intent(this, ProjectListActivity.class)));
        btnSearch.setOnClickListener(v -> startActivity(new Intent(this, SearchActivity.class)));
        btnSync.setOnClickListener(v -> startActivity(new Intent(this, SyncActivity.class)));
    }
}