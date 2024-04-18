package com.example.finalandroidgit;

import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DecimalFormat;
import java.util.Calendar;

public class AddTransactionScreen extends AppCompatActivity {

    int tempMonth, tempDay, tempYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        // binding view objects to variables
        Button homeButton = findViewById(R.id.btnHomeButtonAddTransactionScreen);
        Button saveButton = findViewById(R.id.saveButtonAddTransaction);
        TextView budgetDisplay = findViewById(R.id.budgetCash);
        TextView currentSpending = findViewById(R.id.currentSpendingTotalCash);
        TextView typeInput = findViewById(R.id.transactionTypeInput);
        TextView amountInput = findViewById(R.id.transactionAmountInput);
        TextView dateInput = findViewById(R.id.transactionDateInput);

        // initializing calendar
        DatePickerDialog.OnDateSetListener setListener;
        Calendar calendar = Calendar.getInstance();

        // creating an instance of budget database and then reading from it using the cursor
        MyDatabaseHelperBudget myOtherDB;
        myOtherDB = new MyDatabaseHelperBudget(AddTransactionScreen.this);
        Cursor cursor = myOtherDB.readBudget();
        MyDatabaseHelper myDB = new MyDatabaseHelper(AddTransactionScreen.this);
        Cursor cursorReadTransaction = myDB.readAllData();

        // getting the devices current date
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DecimalFormat twoPlaces = new DecimalFormat("0.00");


        homeButton.setOnClickListener(v -> { // when home button is pressed, app returns to home screen
            finish();
        });

        // reads from the budget and sets the budgetDisplay textview to show the budget value

        if (cursorReadTransaction.getCount() != 0) {
            cursorReadTransaction.moveToLast();
            budgetDisplay.setText("$" + twoPlaces.format(cursorReadTransaction.getDouble(6)));
            if (cursorReadTransaction.getDouble(6) < 0) {
                budgetDisplay.setTextColor(Color.parseColor("#DC143C"));
            } else if (cursorReadTransaction.getDouble(6) > 0) {
                budgetDisplay.setTextColor(Color.parseColor("#008000"));
            } else {
                budgetDisplay.setTextColor(Color.BLACK);
            }
        }
        else if (cursorReadTransaction.getCount() == 0 && cursor.getCount() > 0) {
            cursor.moveToLast();
            budgetDisplay.setText("$" + twoPlaces.format(cursor.getDouble(0)));
            if (cursor.getDouble(0) < 0) {
                budgetDisplay.setTextColor(Color.parseColor("#DC143C"));
            } else if (cursor.getDouble(0) > 0) {
                budgetDisplay.setTextColor(Color.parseColor("#008000"));
            } else {
                budgetDisplay.setTextColor(Color.BLACK);
            }
        }


        // textchangedlistener check for text changed in order to enable the add button
        typeInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                checkInputFields();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputFields();
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkInputFields();
            }
        });

        amountInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                checkInputFields();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputFields();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    currentSpending.setText("$0.00");
                }
                else {
                    currentSpending.setText("$" + s);
                }
                String temp = s.toString();
                try {
                    if (Double.parseDouble(temp) < 0) {
                        currentSpending.setTextColor(Color.parseColor("#DC143C"));
                    } else if (Double.parseDouble(temp) > 0) {
                        currentSpending.setTextColor(Color.parseColor("#008000"));
                    } else {
                        currentSpending.setTextColor(Color.BLACK);
                    }
                }
                catch (NumberFormatException e){
                    currentSpending.setTextColor(Color.BLACK);
                }

                checkInputFields();
            }
        });

        // shows calendar and updates temp date values
        dateInput.setOnClickListener(v -> {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AddTransactionScreen.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month+1;

                        String date = month+"/"+day+"/"+year;
                        dateInput.setText(date);
                        tempMonth = month;
                        tempDay = day;
                        tempYear = year;
                        checkInputFields();
                    }
                },year,month,day);
                datePickerDialog.show();
        });

        // gets values from all input dates and calls addTransaction which adds info to database
        saveButton.setOnClickListener(v -> {
            try {
                myDB.addTransaction(Double.valueOf(amountInput.getText().toString().trim()),
                        typeInput.getText().toString().trim(),
                        tempMonth,
                        tempDay,
                        tempYear);
                Cursor tempCursor = myDB.readAllData();
                tempCursor.moveToLast();
                budgetDisplay.setText("$" + twoPlaces.format(tempCursor.getDouble(6)));
                if (tempCursor.getDouble(6) < 0) {
                    budgetDisplay.setTextColor(Color.parseColor("#DC143C"));
                } else if (tempCursor.getDouble(6) > 0) {
                    budgetDisplay.setTextColor(Color.parseColor("#008000"));
                } else {
                    budgetDisplay.setTextColor(Color.BLACK);
                }
            }
            catch (NumberFormatException e) {
                Toast.makeText(this, "Wrong Input Values!", Toast.LENGTH_SHORT).show();
            }

        });


    }

    // checks if all input fields have information in them before enabling save button
    private void checkInputFields() {
        Button saveButton = findViewById(R.id.saveButtonAddTransaction);
        TextView typeInput = findViewById(R.id.transactionTypeInput);
        TextView amountInput = findViewById(R.id.transactionAmountInput);
        TextView dateInput = findViewById(R.id.transactionDateInput);

        boolean isTypeFilled = !typeInput.getText().toString().trim().isEmpty();
        boolean isAmountFilled = !amountInput.getText().toString().trim().isEmpty();
        boolean isDateFilled = !dateInput.getText().toString().trim().isEmpty();

        saveButton.setEnabled(isTypeFilled && isAmountFilled && isDateFilled);
    }

}