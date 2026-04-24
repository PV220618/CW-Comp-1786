package com.example.expensetracker.activities;
import com.example.expensetracker.R;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.expensetracker.R;
import com.example.expensetracker.database.ExpenseDao;
import com.example.expensetracker.models.Expense;
import com.example.expensetracker.models.Project;
import com.example.expensetracker.utils.DateUtils;
import com.example.expensetracker.utils.ValidationUtils;

public class AddEditExpenseActivity extends AppCompatActivity {

    private Project project;
    private Expense expense;
    private ExpenseDao expenseDao;

    private EditText etExpenseCode;
    private EditText etExpenseDate;
    private EditText etAmount;
    private EditText etClaimant;
    private EditText etDescription;
    private EditText etLocation;

    private AutoCompleteTextView actCurrency;
    private AutoCompleteTextView actExpenseType;
    private AutoCompleteTextView actPaymentMethod;
    private AutoCompleteTextView actPaymentStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_expense);

        project = (Project) getIntent().getSerializableExtra("project");
        if (getIntent().hasExtra("expense")) {
            expense = (Expense) getIntent().getSerializableExtra("expense");
        }

        expenseDao = new ExpenseDao(this);

        bindViews();
        setupDropdowns();

        actCurrency.setKeyListener(null);
        actExpenseType.setKeyListener(null);
        actPaymentMethod.setKeyListener(null);
        actPaymentStatus.setKeyListener(null);

        actCurrency.setOnClickListener(v -> actCurrency.showDropDown());
        actExpenseType.setOnClickListener(v -> actExpenseType.showDropDown());
        actPaymentMethod.setOnClickListener(v -> actPaymentMethod.showDropDown());
        actPaymentStatus.setOnClickListener(v -> actPaymentStatus.showDropDown());

        actCurrency.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) actCurrency.showDropDown();
        });

        actExpenseType.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) actExpenseType.showDropDown();
        });

        actPaymentMethod.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) actPaymentMethod.showDropDown();
        });

        actPaymentStatus.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) actPaymentStatus.showDropDown();
        });

        if (expense != null) {
            populateExpense(expense);
        }

        Button btnSave = findViewById(R.id.btnSaveExpense);
        btnSave.setOnClickListener(v -> saveExpense());
    }

    private void bindViews() {
        etExpenseCode = findViewById(R.id.etExpenseCode);
        etExpenseDate = findViewById(R.id.etExpenseDate);
        etAmount = findViewById(R.id.etAmount);
        etClaimant = findViewById(R.id.etClaimant);
        etDescription = findViewById(R.id.etExpenseDescription);
        etLocation = findViewById(R.id.etLocation);

        actCurrency = findViewById(R.id.actCurrency);
        actExpenseType = findViewById(R.id.actExpenseType);
        actPaymentMethod = findViewById(R.id.actPaymentMethod);
        actPaymentStatus = findViewById(R.id.actPaymentStatus);
    }

    private void setupDropdowns() {
        ArrayAdapter<String> currencyAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                new String[]{"USD"}
        );
        actCurrency.setAdapter(currencyAdapter);
        actCurrency.setThreshold(1);

        ArrayAdapter<String> expenseTypeAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                new String[]{
                        "Travel",
                        "Equipment",
                        "Materials",
                        "Services",
                        "Software/Licenses",
                        "Labour costs",
                        "Utilities",
                        "Miscellaneous"
                }
        );
        actExpenseType.setAdapter(expenseTypeAdapter);
        actExpenseType.setThreshold(1);

        ArrayAdapter<String> paymentMethodAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                new String[]{
                        "Cash",
                        "Credit Card",
                        "Bank Transfer",
                        "Cheque"
                }
        );
        actPaymentMethod.setAdapter(paymentMethodAdapter);
        actPaymentMethod.setThreshold(1);

        ArrayAdapter<String> paymentStatusAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                new String[]{
                        "Paid",
                        "Pending",
                        "Reimbursed"
                }
        );
        actPaymentStatus.setAdapter(paymentStatusAdapter);
        actPaymentStatus.setThreshold(1);
    }

    private void populateExpense(Expense e) {
        etExpenseCode.setText(e.getExpenseCode());
        etExpenseDate.setText(e.getExpenseDate());
        etAmount.setText(String.valueOf(e.getAmount()));
        actCurrency.setText(e.getCurrency(), false);
        actExpenseType.setText(e.getExpenseType(), false);
        actPaymentMethod.setText(e.getPaymentMethod(), false);
        etClaimant.setText(e.getClaimant());
        actPaymentStatus.setText(e.getPaymentStatus(), false);
        etDescription.setText(e.getDescription());
        etLocation.setText(e.getLocation());
    }

    private void saveExpense() {
        String expenseCode = etExpenseCode.getText().toString().trim();
        String expenseDate = etExpenseDate.getText().toString().trim();
        String amountValue = etAmount.getText().toString().trim();
        String currency = actCurrency.getText().toString().trim();
        String expenseType = actExpenseType.getText().toString().trim();
        String paymentMethod = actPaymentMethod.getText().toString().trim();
        String claimant = etClaimant.getText().toString().trim();
        String paymentStatus = actPaymentStatus.getText().toString().trim();

        if (ValidationUtils.isEmpty(expenseCode)
                || ValidationUtils.isEmpty(expenseDate)
                || !ValidationUtils.isPositiveAmount(amountValue)
                || ValidationUtils.isEmpty(currency)
                || ValidationUtils.isEmpty(expenseType)
                || ValidationUtils.isEmpty(paymentMethod)
                || ValidationUtils.isEmpty(claimant)
                || ValidationUtils.isEmpty(paymentStatus)) {

            Toast.makeText(this, "Please complete all required expense fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Expense entity = (expense != null) ? expense : new Expense();
        entity.setExpenseCode(expenseCode);
        entity.setProjectId(project.getId());
        entity.setExpenseDate(expenseDate);
        entity.setAmount(Double.parseDouble(amountValue));
        entity.setCurrency(currency);
        entity.setExpenseType(expenseType);
        entity.setPaymentMethod(paymentMethod);
        entity.setClaimant(claimant);
        entity.setPaymentStatus(paymentStatus);
        entity.setDescription(etDescription.getText().toString().trim());
        entity.setLocation(etLocation.getText().toString().trim());
        entity.setIsSynced(0);
        entity.setUpdatedAt(DateUtils.now());

        if (expense == null) {
            entity.setCreatedAt(DateUtils.now());
            expenseDao.insert(entity);
        } else {
            expenseDao.update(entity);
        }

        Toast.makeText(this, "Expense saved", Toast.LENGTH_SHORT).show();
        finish();
    }
}