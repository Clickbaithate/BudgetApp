package com.example.finalandroidgit;

import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;

public class UpdateActivity extends AppCompatActivity {

    int tempMonth;
    int tempDay;
    int tempYear;

    double prevAmount;

    String id, amount, name, Zmonth, Zday, Zyear, date, remaining;

    TextView budgetDisplay, currentSpending, typeInput, amountInput, dateInput;
    Button homeButton, updateButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        homeButton = findViewById(R.id.btnHomeButtonUpdateTransactionScreen);
        updateButton = findViewById(R.id.saveButtonUpdateTransaction);

        budgetDisplay = findViewById(R.id.budgetCash2);
        currentSpending = findViewById(R.id.currentSpendingTotalCash2);

        typeInput = findViewById(R.id.transactionTypeInput2);
        amountInput = findViewById(R.id.transactionAmountInput2);

        MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateActivity.this);
        MyDatabaseHelperBudget myDBbudget = new MyDatabaseHelperBudget(UpdateActivity.this);
        Cursor cursorBudget = myDBbudget.readBudget();
        Cursor cursorTrans = myDB.readAllData();

        dateInput = findViewById(R.id.updateTransactionDateInput);
        DatePickerDialog.OnDateSetListener setListener;
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        homeButton.setOnClickListener(v -> {
            finish();
        });

        getAndSetIntentData();

        double budgetTemp = Double.parseDouble(remaining);
        budgetDisplay.setText("$" + budgetTemp);
        if (budgetTemp < 0) {
            budgetDisplay.setTextColor(Color.parseColor("#DC143C"));
        }
        else if (budgetTemp > 0) {
            budgetDisplay.setTextColor(Color.parseColor("#008000"));
        }
        else {
            budgetDisplay.setTextColor(Color.BLACK);
        }

        currentSpending.setText(amount);
        if ((amount.length() == 1 && amount.charAt(0) != '-') || (amount.length() != 0)) {
            if (amount.charAt(0) == '-' && amount.length() == 1) {
                currentSpending.setTextColor(Color.parseColor("#DC143C"));
            }
            else {
                if (Double.parseDouble(amount) < 0) {
                    currentSpending.setTextColor(Color.parseColor("#DC143C"));
                } else if (Double.parseDouble(amount) > 0) {
                    currentSpending.setTextColor(Color.parseColor("#008000"));
                } else {
                    currentSpending.setTextColor(Color.BLACK);
                }
            }
        }
        else if ((amount.length() == 0)) {
            currentSpending.setTextColor(Color.BLACK);
        }
        if (amount.length() == 1 && amount.charAt(0) == '-') {
            currentSpending.setTextColor(Color.parseColor("#DC143C"));
        }




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
                    currentSpending.setTextColor(Color.BLACK);
                } else {
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

        dateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        UpdateActivity.this, new DatePickerDialog.OnDateSetListener() {
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

            }

        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                double currentBudget = 0;
                double totalSpent = 0;

                // grabbing all input info, even if changed or unchanged
                name = typeInput.getText().toString();
                String date;
                if (tempMonth != 0 && tempDay != 0 && tempYear != 0) {
                    // User has updated the date, so use the new date
                    date = tempMonth+"/"+tempDay+"/"+tempYear;
                } else {
                    // User has not updated the date, so use the existing date
                    date = Zmonth+"/"+Zday+"/"+Zyear;
                }
                dateInput.setText(date);
                dateInput.setText(date);
                Zmonth = tempMonth+""; Zday = tempDay+""; Zyear = tempYear+"";
                try {amount = amountInput.getText().toString();
                } catch (NumberFormatException e) {
                    Toast.makeText(UpdateActivity.this, "Wrong Input Values!", Toast.LENGTH_SHORT).show();
                }

                int currentID = Integer.parseInt(id);

                // recalculating remaining after transaction amount has been changed
                if (cursorBudget.moveToFirst()) {
                    currentBudget = cursorBudget.getDouble(0);
                }
                if (cursorTrans.getCount() != 0) {
                    while (cursorTrans.moveToNext()) {
                        if (cursorTrans.getInt(0) > currentID ) {
                            break;
                        }
                        else if (cursorTrans.getInt(0) < currentID) {
                            totalSpent += cursorTrans.getDouble(1);
                        }

                    }
                }

                double remaining = -1;

                try {
                    remaining = currentBudget + (totalSpent) + Double.parseDouble(amount);
                } catch (NumberFormatException e) {
                    //Toast.makeText(UpdateActivity.this, "Wrong Input Values!", Toast.LENGTH_SHORT).show();
                }

                // updating current transactions data
                try {
                    myDB.updateTransaction(id, Double.parseDouble(amount), name, Integer.parseInt(Zmonth)
                            , Integer.parseInt(Zday), Integer.parseInt(Zyear), remaining);
                    Toast.makeText(UpdateActivity.this, "Edit Successful!", Toast.LENGTH_SHORT).show();
                } catch (NumberFormatException e) {
                    Toast.makeText(UpdateActivity.this, "Wrong Input Values!", Toast.LENGTH_SHORT).show();
                }

                Cursor cursor = myDB.readAllData();
                Cursor cursorTEMP = myDB.readAllData();
                cursorTEMP.moveToFirst();
                double topTrans = cursorTEMP.getDouble(1);

                int tempID = 0;

                // recalculating budget remaining for every transaction after updated transaction
                // this is because changing one transactions value affects every other transactions remaining budget value
                while (cursor.moveToNext()) {
                    cursorTEMP.moveToFirst();
                    if (currentID == 1) {
                        if (cursor.getInt(0) > currentID) {

                            tempID = cursor.getInt(0);
                            totalSpent = Double.parseDouble(amount);

                            while (cursorTEMP.moveToNext()) {
                                if (cursorTEMP.getInt(0) <= tempID) {
                                    totalSpent += cursorTEMP.getDouble(1);
                                }
                            }
                            remaining = currentBudget + totalSpent;


                            myDB.updateTransaction(String.valueOf(tempID), cursor.getDouble(1),
                                    cursor.getString(2), cursor.getInt(3), cursor.getInt(4),
                                    cursor.getInt(5), remaining);

                        }
                    }
                    else {
                        if (cursor.getInt(0) >= currentID) {

                            tempID = cursor.getInt(0);
                            totalSpent = 0;

                            while (cursorTEMP.moveToNext()) {
                                if (cursorTEMP.getInt(0) <= tempID) {
                                    totalSpent += cursorTEMP.getDouble(1);
                                }
                            }
                            remaining = currentBudget + totalSpent + topTrans;


                            myDB.updateTransaction(String.valueOf(tempID), cursor.getDouble(1),
                                    cursor.getString(2), cursor.getInt(3), cursor.getInt(4),
                                    cursor.getInt(5), remaining);

                        }
                    }
                }


                finish();
            }
        });





    }

    void getAndSetIntentData() {
        if (getIntent().hasExtra("id") && getIntent().hasExtra("amount")
                && getIntent().hasExtra("name") && getIntent().hasExtra("date")
                && getIntent().hasExtra("remaining")) {

            id = getIntent().getStringExtra("id");
            amount = getIntent().getStringExtra("amount");
            name = getIntent().getStringExtra("name");
            Zmonth = getIntent().getStringExtra("month");
            Zday = getIntent().getStringExtra("day");
            Zyear = getIntent().getStringExtra("year");
            date = getIntent().getStringExtra("date");
            remaining = getIntent().getStringExtra("remaining");

            tempMonth = Integer.parseInt(Zmonth);
            tempDay = Integer.parseInt(Zday);
            tempYear = Integer.parseInt(Zyear);

            amountInput.setText(amount);
            typeInput.setText(name);
            dateInput.setText(date);


        }
        else {
            Toast.makeText(this,R.string.toast_no_data, Toast.LENGTH_SHORT).show();
        }
    }

    private void checkInputFields() {
        Button saveButton = findViewById(R.id.saveButtonUpdateTransaction);
        TextView typeInput = findViewById(R.id.transactionTypeInput2);
        TextView amountInput = findViewById(R.id.transactionAmountInput2);
        TextView dateInput = findViewById(R.id.updateTransactionDateInput);

        boolean isTypeFilled = !typeInput.getText().toString().trim().isEmpty();
        boolean isAmountFilled = !amountInput.getText().toString().trim().isEmpty();
        boolean isDateFilled = !dateInput.getText().toString().trim().isEmpty();

        saveButton.setEnabled(isTypeFilled && isAmountFilled && isDateFilled);
    }

}