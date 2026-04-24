package com.example.expensetracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.expensetracker.R;
import com.example.expensetracker.database.ProjectDao;
import com.example.expensetracker.models.Project;
import com.example.expensetracker.utils.DateUtils;
import com.example.expensetracker.utils.ValidationUtils;

public class AddEditProjectActivity extends AppCompatActivity {

    private EditText etProjectCode;
    private EditText etName;
    private EditText etDescription;
    private EditText etStartDate;
    private EditText etEndDate;
    private EditText etOwner;
    private EditText etBudget;
    private EditText etSpecialReq;
    private EditText etClientInfo;
    private EditText etPriority;
    private EditText etRiskLevel;
    private EditText etContactEmail;

    private AutoCompleteTextView actStatus;

    private Project existingProject;
    private ProjectDao projectDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_project);

        projectDao = new ProjectDao(this);

        bindViews();
        setupDropdowns();

        actStatus.setKeyListener(null);
        actStatus.setOnClickListener(v -> actStatus.showDropDown());
        actStatus.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                actStatus.showDropDown();
            }
        });

        if (getIntent().hasExtra("project")) {
            existingProject = (Project) getIntent().getSerializableExtra("project");
            populateData(existingProject);
        }

        Button btnConfirm = findViewById(R.id.btnConfirmProject);
        btnConfirm.setOnClickListener(v -> validateAndConfirm());
    }

    private void bindViews() {
        etProjectCode = findViewById(R.id.etProjectCode);
        etName = findViewById(R.id.etProjectName);
        etDescription = findViewById(R.id.etProjectDescription);
        etStartDate = findViewById(R.id.etStartDate);
        etEndDate = findViewById(R.id.etEndDate);
        etOwner = findViewById(R.id.etOwner);
        actStatus = findViewById(R.id.actStatus);
        etBudget = findViewById(R.id.etBudget);
        etSpecialReq = findViewById(R.id.etSpecialReq);
        etClientInfo = findViewById(R.id.etClientInfo);
        etPriority = findViewById(R.id.etPriority);
        etRiskLevel = findViewById(R.id.etRiskLevel);
        etContactEmail = findViewById(R.id.etContactEmail);
    }

    private void setupDropdowns() {
        String[] statuses = {"Active", "Completed", "On Hold"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                statuses
        );

        actStatus.setAdapter(adapter);
        actStatus.setThreshold(1);
    }

    private void populateData(Project p) {
        etProjectCode.setText(p.getProjectCode());
        etName.setText(p.getName());
        etDescription.setText(p.getDescription());
        etStartDate.setText(p.getStartDate());
        etEndDate.setText(p.getEndDate());
        etOwner.setText(p.getOwner());
        actStatus.setText(p.getStatus(), false);
        etBudget.setText(String.valueOf(p.getBudget()));
        etSpecialReq.setText(p.getSpecialRequirements());
        etClientInfo.setText(p.getClientInfo());
        etPriority.setText(p.getPriority());
        etRiskLevel.setText(p.getRiskLevel());
        etContactEmail.setText(p.getContactEmail());
    }



    private void validateAndConfirm() {
        String code = etProjectCode.getText().toString().trim();
        String name = etName.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String startDate = etStartDate.getText().toString().trim();
        String endDate = etEndDate.getText().toString().trim();
        String owner = etOwner.getText().toString().trim();
        String status = actStatus.getText().toString().trim();
        String budgetValue = etBudget.getText().toString().trim();
        String email = etContactEmail.getText().toString().trim();

        if (ValidationUtils.isEmpty(code)
                || ValidationUtils.isEmpty(name)
                || ValidationUtils.isEmpty(description)
                || ValidationUtils.isEmpty(startDate)
                || ValidationUtils.isEmpty(endDate)
                || ValidationUtils.isEmpty(owner)
                || ValidationUtils.isEmpty(status)
                || ValidationUtils.isEmpty(budgetValue)) {

            Toast.makeText(this, "Please complete all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!ValidationUtils.isPositiveAmount(budgetValue)) {
            Toast.makeText(this, "Budget must be greater than 0", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!ValidationUtils.isEndDateValid(startDate, endDate)) {
            Toast.makeText(this, "End date must be after or equal to start date", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!ValidationUtils.isValidEmail(email)) {
            Toast.makeText(this, "Invalid contact email", Toast.LENGTH_SHORT).show();
            return;
        }

        Integer excludeId = existingProject != null ? existingProject.getId() : null;
        if (projectDao.existsProjectCode(code, excludeId)) {
            Toast.makeText(this, "Project code already exists", Toast.LENGTH_SHORT).show();
            return;
        }

        Project project = existingProject != null ? existingProject : new Project();
        project.setProjectCode(code);
        project.setName(name);
        project.setDescription(description);
        project.setStartDate(startDate);
        project.setEndDate(endDate);
        project.setOwner(owner);
        project.setStatus(status);
        project.setBudget(Double.parseDouble(budgetValue));
        project.setSpecialRequirements(etSpecialReq.getText().toString().trim());
        project.setClientInfo(etClientInfo.getText().toString().trim());
        project.setPriority(etPriority.getText().toString().trim());
        project.setRiskLevel(etRiskLevel.getText().toString().trim());
        project.setContactEmail(email);
        project.setUpdatedAt(DateUtils.now());
        project.setIsSynced(0);

        if (existingProject == null) {
            project.setCreatedAt(DateUtils.now());
        }

        Intent intent = new Intent(this, ConfirmProjectActivity.class);
        intent.putExtra("project", project);
        startActivity(intent);
    }
}