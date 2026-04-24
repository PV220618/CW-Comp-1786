package com.example.expensetracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.R;
import com.example.expensetracker.adapters.ExpenseAdapter;
import com.example.expensetracker.database.ExpenseDao;
import com.example.expensetracker.models.Expense;
import com.example.expensetracker.models.Project;

import java.util.List;

public class ExpenseListActivity extends AppCompatActivity implements ExpenseAdapter.ExpenseClickListener {
    private Project project;
    private ExpenseDao expenseDao;
    private ExpenseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_list);

        project = (Project) getIntent().getSerializableExtra("project");
        expenseDao = new ExpenseDao(this);

        RecyclerView recyclerView = findViewById(R.id.recyclerExpenses);
        Button btnAddExpense = findViewById(R.id.btnAddExpense);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ExpenseAdapter(this, this);
        recyclerView.setAdapter(adapter);

        btnAddExpense.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddEditExpenseActivity.class);
            intent.putExtra("project", project);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<Expense> expenses = expenseDao.getExpensesByProjectId(project.getId());
        adapter.setExpenses(expenses);
    }

    @Override
    public void onExpenseEdit(Expense expense) {
        Intent intent = new Intent(this, AddEditExpenseActivity.class);
        intent.putExtra("project", project);
        intent.putExtra("expense", expense);
        startActivity(intent);
    }

    @Override
    public void onExpenseDelete(Expense expense) {
        expenseDao.delete(expense.getId(), expense.getProjectId());
        adapter.setExpenses(expenseDao.getExpensesByProjectId(project.getId()));
    }
}