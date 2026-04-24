package com.example.expensetracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.expensetracker.R;
import com.example.expensetracker.database.ProjectDao;
import com.example.expensetracker.models.Project;

public class ProjectDetailActivity extends AppCompatActivity {

    private Project project;
    private ProjectDao projectDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detail);

        projectDao = new ProjectDao(this);

        Button btnEdit = findViewById(R.id.btnEditProjectDetail);
        Button btnExpenses = findViewById(R.id.btnViewExpenses);
        TextView tvProjectDetail = findViewById(R.id.tvProjectDetail);
        ProgressBar progressBudget = findViewById(R.id.progressBudget);

        project = (Project) getIntent().getSerializableExtra("project");
        if (project != null) {
            project = projectDao.getProjectById(project.getId());
        }

        if (project != null) {
            tvProjectDetail.setText(buildDetail(project));
            int progress = project.getBudget() > 0
                    ? (int) Math.min(100, (project.getTotalExpense() / project.getBudget()) * 100)
                    : 0;
            progressBudget.setProgress(progress);
        }

        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddEditProjectActivity.class);
            intent.putExtra("project", project);
            startActivity(intent);
        });

        btnExpenses.setOnClickListener(v -> {
            Intent intent = new Intent(this, ExpenseListActivity.class);
            intent.putExtra("project", project);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (project != null) {
            project = projectDao.getProjectById(project.getId());

            TextView tvProjectDetail = findViewById(R.id.tvProjectDetail);
            ProgressBar progressBudget = findViewById(R.id.progressBudget);

            tvProjectDetail.setText(buildDetail(project));

            int progress = project.getBudget() > 0
                    ? (int) Math.min(100, (project.getTotalExpense() / project.getBudget()) * 100)
                    : 0;
            progressBudget.setProgress(progress);
        }
    }

    private String buildDetail(Project p) {
        return "Project Code: " + p.getProjectCode() + "\n"
                + "Name: " + p.getName() + "\n"
                + "Owner: " + p.getOwner() + "\n"
                + "Status: " + p.getStatus() + "\n"
                + "Budget: " + p.getBudget() + "\n"
                + "Total Expenses: " + p.getTotalExpense() + "\n"
                + "Description: " + p.getDescription();
    }
}