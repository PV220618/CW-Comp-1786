package com.example.expensetracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.R;
import com.example.expensetracker.adapters.ProjectAdapter;
import com.example.expensetracker.database.ProjectDao;
import com.example.expensetracker.models.Project;

import java.util.List;

public class SearchActivity extends AppCompatActivity implements ProjectAdapter.ProjectClickListener {
    private ProjectDao projectDao;
    private ProjectAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        projectDao = new ProjectDao(this);

        EditText etKeyword = findViewById(R.id.etSearchKeyword);
        EditText etOwner = findViewById(R.id.etSearchOwner);
        EditText etDate = findViewById(R.id.etSearchDate);
        AutoCompleteTextView actStatus = findViewById(R.id.actSearchStatus);
        Button btnSearch = findViewById(R.id.btnSearchProjects);
        RecyclerView recyclerView = findViewById(R.id.recyclerSearchResults);

        actStatus.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line,
                new String[]{"", "Active", "Completed", "On Hold"}));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProjectAdapter(this, this);
        recyclerView.setAdapter(adapter);

        btnSearch.setOnClickListener(v -> {
            List<Project> results = projectDao.searchProjects(
                    etKeyword.getText().toString().trim(),
                    etOwner.getText().toString().trim(),
                    actStatus.getText().toString().trim(),
                    etDate.getText().toString().trim()
            );
            adapter.setProjects(results);
        });
    }

    @Override
    public void onProjectClick(Project project) {
        Intent intent = new Intent(this, ProjectDetailActivity.class);
        intent.putExtra("project", project);
        startActivity(intent);
    }

    @Override
    public void onProjectDelete(Project project) {}

    @Override
    public void onProjectEdit(Project project) {}
}