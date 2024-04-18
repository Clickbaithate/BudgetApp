package com.example.finalandroidgit;

import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.DatePicker;
import android.widget.Toast;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.text.DecimalFormat;

public class TransactionsInTimeFrame extends AppCompatActivity {

    int topMonth, topDay, topYear;
    int bottomMonth, bottomDay, bottomYear;
    double total = 0, mostExpensive = 0, avgDailyValue = 0;
    int tempMonth, tempDay, tempYear;
    double tempVal;
    double counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_in_time_frame);
        DecimalFormat twoPlaces = new DecimalFormat("0.00");
        Button saveButton = findViewById(R.id.saveButtonTimeFrame);
        Button homeButton = findViewById(R.id.btnHomeButtonTimeFrameScreen);
        TextView topDateInput = findViewById(R.id.topInputDateText);
        TextView bottomDateInput = findViewById(R.id.bottomInputDateText);
        TextView totalExpensesText = findViewById(R.id.expensesWithinTimeRangeCash);
        TextView avgDailyText = findViewById(R.id.avgDailySpendingCash);
        TextView mostExpensiveText = findViewById(R.id.mostExpensiveCash);

        DatePickerDialog.OnDateSetListener setListener;
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        MyDatabaseHelper myDB = new MyDatabaseHelper(TransactionsInTimeFrame.this);
        Cursor cursor = myDB.readAllData();

        homeButton.setOnClickListener(v -> {
            finish();
        });

        topDateInput.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    TransactionsInTimeFrame.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    month = month+1;

                    String date = month+"/"+day+"/"+year;
                    topDateInput.setText(date);
                    topMonth = month;
                    topDay = day;
                    topYear = year;
                    checkInputFields();
                }
            },year,month,day);
            datePickerDialog.show();
        });

        bottomDateInput.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    TransactionsInTimeFrame.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    month = month+1;

                    String date = month+"/"+day+"/"+year;
                    bottomDateInput.setText(date);
                    bottomMonth = month;
                    bottomDay = day;
                    bottomYear = year;
                    checkInputFields();
                }
            },year,month,day);
            datePickerDialog.show();
        });

        saveButton.setOnClickListener(v ->{
            if (bottomDay < topDay || bottomMonth < topMonth || bottomYear < topYear) { // user input check
                Toast.makeText(this, "Please Select a Valid Date Range", Toast.LENGTH_SHORT).show();
                return;
            }
            total = 0; mostExpensive = 0; avgDailyValue = 0; counter = 0; // reset values to 0

            cursor.moveToFirst(); // moving database reading back to beginning
            do {
                // From table, setting tempVariables to their respective values
                tempYear = cursor.getInt(5);
                tempMonth = cursor.getInt(3);
                tempDay = cursor.getInt(4);
                tempVal = cursor.getDouble(1);

                // Checks if dates are in range
                if (tempYear >= topYear && tempYear <= bottomYear){
                    if (tempMonth >= topMonth && tempMonth <= bottomMonth){
                        if (tempDay >= topDay && tempDay <= bottomDay){
                            // Total for the range is calculated here
                            total += tempVal;

                            // Counter increments to use for avg later
                            counter++;

                            // Most expensive calculated here (checks if less than mostExpensive
                            // so it counts negatives correctly)
                            if (tempVal < mostExpensive){
                                mostExpensive = tempVal;
                            }
                        }
                    }
                }

            } while (cursor.moveToNext());

            // Text is set
            mostExpensiveText.setText("$" + twoPlaces.format(mostExpensive));
            totalExpensesText.setText("$" + twoPlaces.format(total));

            // If counter is above 0, calculations are done and text is set
            // otherwise, stays at the text it starts at
            if (counter != 0){
                LocalDate start = LocalDate.of(topYear, topMonth, topDay);
                LocalDate end = LocalDate.of(bottomYear, bottomMonth, bottomDay);
                // Average Daily actually calculated outside of whileLoop
                long numDays = ChronoUnit.DAYS.between(start, end);
                numDays += 1;
                avgDailyValue = total / numDays; // total / counter instead of avgDailyValue / counter to prevent changing the value
                avgDailyText.setText("$" + twoPlaces.format(avgDailyValue));
            }

        });
    }

    // checks if two dates have been inputted, if not dont enable saveButton
    private void checkInputFields() {
        Button saveButton = findViewById(R.id.saveButtonTimeFrame);
        TextView topDateInput = findViewById(R.id.topInputDateText);
        TextView bottomDateInput = findViewById(R.id.bottomInputDateText);

        boolean isTopDate = !topDateInput.getText().toString().trim().isEmpty();
        boolean isBottomDate = !bottomDateInput.getText().toString().trim().isEmpty();

        saveButton.setEnabled(isTopDate && isBottomDate);
    }

}