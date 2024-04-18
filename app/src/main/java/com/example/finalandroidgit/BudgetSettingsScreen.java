package com.example.finalandroidgit;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;

public class BudgetSettingsScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_settings);

        Button BTN_Reset = findViewById(R.id.resetButton);
        Button BTN_budgetSubmit = findViewById(R.id.BTN_budgetValueSubmit);
        Button homeButton = findViewById(R.id.btnHomeButtonSpendingScreen);
        TextView TV_budgetValue = findViewById(R.id.TV_budgetValue);

        MyDatabaseHelperBudget budgetDB = new MyDatabaseHelperBudget(BudgetSettingsScreen.this);
        MyDatabaseHelper transactionsDB = new MyDatabaseHelper(BudgetSettingsScreen.this);
        Cursor cursor = budgetDB.readBudget();

        // checks if there is a budget in order to enable addBudget or resetBudget button
        if (cursor.getCount() > 0) {
            Toast.makeText(BudgetSettingsScreen.this, R.string.toast_already, Toast.LENGTH_SHORT).show();
            BTN_budgetSubmit.setEnabled(false);
            TV_budgetValue.setEnabled(false);
            BTN_Reset.setEnabled(true);
        }
        else if (cursor.getCount() == 0) {
            BTN_Reset.setEnabled(false);
        }

        homeButton.setOnClickListener(v -> {
            finish();
        });

        // checks if budget input field is empty before adding that value to the budget database
        BTN_budgetSubmit.setOnClickListener(v -> {
            try {
                budgetDB.addBudget(Double.valueOf(TV_budgetValue.getText().toString().trim()));
                finish();
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Enter Valid Input!", Toast.LENGTH_SHORT).show();
            }

        });

        // verifies that you want to reset budget and all previous transactions
        BTN_Reset.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(BudgetSettingsScreen.this);
            builder.setMessage(R.string.alert_message_text);
            builder.setTitle(R.string.alert_title_text);

            builder.setPositiveButton(R.string.alert_pos_choice, (DialogInterface.OnClickListener) (dialog, which) -> {
                SQLiteDatabase temp = transactionsDB.getReadableDatabase();
                SQLiteDatabase secondTemp = budgetDB.getReadableDatabase();

                transactionsDB.deleteData(temp);
                budgetDB.deleteData(secondTemp);

                BTN_budgetSubmit.setEnabled(true);
                TV_budgetValue.setEnabled(true);
                BTN_Reset.setEnabled(false);
            });

            builder.setNegativeButton(R.string.alert_neg_choice, (DialogInterface.OnClickListener) (dialog, which) -> dialog.cancel());

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });




    }
}
