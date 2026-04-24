package com.example.expensetracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.R;
import com.example.expensetracker.adapters.ProjectAdapter;
import com.example.expensetracker.database.DatabaseHelper;
import com.example.expensetracker.database.ProjectDao;
import com.example.expensetracker.models.Project;

import java.util.List;

public class ProjectListActivity extends AppCompatActivity implements ProjectAdapter.ProjectClickListener {

    private ProjectDao projectDao;
    private ProjectAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_list);

        projectDao = new ProjectDao(this);

        RecyclerView recyclerView = findViewById(R.id.recyclerProjects);
        Button btnAddProject = findViewById(R.id.btnAddProject);
        Button btnResetDb = findViewById(R.id.btnResetDb);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProjectAdapter(this, this);
        recyclerView.setAdapter(adapter);

        btnAddProject.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddEditProjectActivity.class);
            startActivity(intent);
        });

        btnResetDb.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Reset Database")
                    .setMessage("Delete all projects and expenses?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        new DatabaseHelper(this).resetDatabase();
                        loadData();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        List<Project> projects = projectDao.getAllProjects();
        adapter.setProjects(projects);
    }

    @Override
    public void onProjectClick(Project project) {
        Intent intent = new Intent(this, ProjectDetailActivity.class);
        intent.putExtra("project", project);
        startActivity(intent);
    }

    @Override
    public void onProjectDelete(Project project) {
        projectDao.delete(project.getId());
        loadData();
    }

    @Override
    public void onProjectEdit(Project project) {
        Intent intent = new Intent(this, AddEditProjectActivity.class);
        intent.putExtra("project", project);
        startActivity(intent);
    }
}