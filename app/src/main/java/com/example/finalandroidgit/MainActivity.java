package com.example.finalandroidgit;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Color;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    ////////////////////////// TextViews of Recent Transaction Elements Only

    RelativeLayout firstRecent, secondRecent, thirdRecent;

    TextView TV_RecentID, TV_RecentAmount, TV_RecentName, TV_RecentRemaining, TV_RecentDate;
    TextView TV_2RecentID, TV_2RecentAmount, TV_2RecentName, TV_2RecentRemaining, TV_2RecentDate;
    TextView TV_3RecentID, TV_3RecentAmount, TV_3RecentName, TV_3RecentRemaining, TV_3RecentDate;

    int randomCount;

    //////////////////////////

    TextView TVremaining, TVbudget;
    Button spendingHistory, budgetTimeFrame, budgetSettings;
    MyDatabaseHelper myDB;
    MyDatabaseHelperBudget myOtherDB;

    Button addTransaction;
    DecimalFormat twoPlaces = new DecimalFormat("0.00");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstRecent = findViewById(R.id.BLOCK_mostRecentTransaction);
        secondRecent = findViewById(R.id.BLOCK_2ndMostRecentTransaction);
        thirdRecent = findViewById(R.id.BLOCK_3rdMostRecentTransaction);

        randomCount = 0;

        ////

        TV_RecentID = findViewById(R.id.TV_recentID);
        TV_RecentAmount = findViewById(R.id.TV_recentAmount);
        TV_RecentName = findViewById(R.id.TV_recentName);
        TV_RecentRemaining = findViewById(R.id.TV_recentRemaining);
        TV_RecentDate = findViewById(R.id.TV_recentDate);

        TV_2RecentID = findViewById(R.id.TV_2recentID);
        TV_2RecentAmount = findViewById(R.id.TV_2recentAmount);
        TV_2RecentName = findViewById(R.id.TV_2recentName);
        TV_2RecentRemaining = findViewById(R.id.TV_2recentRemaining);
        TV_2RecentDate = findViewById(R.id.TV_2recentDate);

        TV_3RecentID = findViewById(R.id.TV_3recentID);
        TV_3RecentAmount = findViewById(R.id.TV_3recentAmount);
        TV_3RecentName = findViewById(R.id.TV_3recentName);
        TV_3RecentRemaining = findViewById(R.id.TV_3recentRemaining);
        TV_3RecentDate = findViewById(R.id.TV_3recentDate);

        ////

        addTransaction = findViewById(R.id.btnAddTransaction);
        spendingHistory = findViewById(R.id.btnRevealSpendingHistory);
        budgetTimeFrame = findViewById(R.id.btnBudgetTimeFrame);
        budgetSettings = findViewById(R.id.btnBudgetSettings);

        TVremaining = findViewById(R.id.tvHomePageRemainingValue);
        TVbudget = findViewById(R.id.tvHomePageBudgetValue);

        Intent addTransactionScreen = new Intent(this, AddTransactionScreen.class);
        Intent spendingHistoryScreen = new Intent(this, SpendingHistoryScreen.class);
        Intent TransactionsInTimeFrameScreen = new Intent(this, TransactionsInTimeFrame.class);
        Intent budgetSettingsScreen = new Intent(this, BudgetSettingsScreen.class);

        spendingHistory.setOnClickListener(view -> {
            startActivity(spendingHistoryScreen);
        });

        addTransaction.setOnClickListener(view ->{
            startActivity(addTransactionScreen);
        });

        budgetTimeFrame.setOnClickListener(view ->{
            startActivity(TransactionsInTimeFrameScreen);
        });

        budgetSettings.setOnClickListener(view -> {
            startActivity(budgetSettingsScreen);
        });

        myDB = new MyDatabaseHelper(MainActivity.this);
        myOtherDB = new MyDatabaseHelperBudget(MainActivity.this);

        updateRemaining();
        checkIfBudget();
        checkTransactionCount();
        updateRecent();

    }

    // makes sure to check if a transaction was added after going from the addTran screen to home
    void updateRecent() {

        Cursor randomCursor = myDB.rearrangeDatabase(); // reads database from last to first

        if (randomCursor.getCount() == 0) {
            return;
        }

        if (randomCursor.getCount() == 1) { // checks how many transactions there are in order to update properly
            randomCursor.moveToFirst();
            TV_RecentID.setText("1");
            TV_RecentAmount.setText(twoPlaces.format(randomCursor.getDouble(1)));
            TV_RecentName.setText(randomCursor.getString(2));
            TV_RecentDate.setText(randomCursor.getInt(3) + "/" + randomCursor.getInt(4) + "/" + randomCursor.getInt(5));
            TV_RecentRemaining.setText(twoPlaces.format(randomCursor.getDouble(6)));
        }
        else if (randomCursor.getCount() == 2) {
            randomCursor.moveToFirst();
            for (int i = 0; i < 2; i++) {

                if (i == 0) {
                    TV_RecentID.setText("1");
                    TV_RecentAmount.setText(twoPlaces.format(randomCursor.getDouble(1)));
                    TV_RecentName.setText(randomCursor.getString(2));
                    TV_RecentDate.setText(randomCursor.getInt(3) + "/" + randomCursor.getInt(4) + "/" + randomCursor.getInt(5));
                    TV_RecentRemaining.setText(twoPlaces.format(randomCursor.getDouble(6)));
                } else {
                    TV_2RecentID.setText("2");
                    TV_2RecentAmount.setText(twoPlaces.format(randomCursor.getDouble(1)));
                    TV_2RecentName.setText(randomCursor.getString(2));
                    TV_2RecentDate.setText(randomCursor.getInt(3) + "/" + randomCursor.getInt(4) + "/" + randomCursor.getInt(5));
                    TV_2RecentRemaining.setText(twoPlaces.format(randomCursor.getDouble(6)));
                }

                randomCursor.moveToNext();
            }
        }
        else {
            randomCursor.moveToFirst();
            for (int i = 0; i < 3; i++) {

                if (i == 0) {
                    TV_RecentID.setText("1");
                    TV_RecentAmount.setText(twoPlaces.format(randomCursor.getDouble(1)));
                    TV_RecentName.setText(randomCursor.getString(2));
                    TV_RecentDate.setText(randomCursor.getInt(3) + "/" + randomCursor.getInt(4) + "/" + randomCursor.getInt(5));
                    TV_RecentRemaining.setText(twoPlaces.format(randomCursor.getDouble(6)));
                } else if (i == 1) {
                    TV_2RecentID.setText("2");
                    TV_2RecentAmount.setText(twoPlaces.format(randomCursor.getDouble(1)));
                    TV_2RecentName.setText(randomCursor.getString(2));
                    TV_2RecentDate.setText(randomCursor.getInt(3) + "/" + randomCursor.getInt(4) + "/" + randomCursor.getInt(5));
                    TV_2RecentRemaining.setText(twoPlaces.format(randomCursor.getDouble(6)));
                } else {
                    TV_3RecentID.setText("3");
                    TV_3RecentAmount.setText(twoPlaces.format(randomCursor.getDouble(1)));
                    TV_3RecentName.setText(randomCursor.getString(2));
                    TV_3RecentDate.setText(randomCursor.getInt(3) + "/" + randomCursor.getInt(4) + "/" + randomCursor.getInt(5));
                    TV_3RecentRemaining.setText(twoPlaces.format(randomCursor.getDouble(6)));
                }

                randomCursor.moveToNext();
            }
        }



    }

    // checks if there is any transactions, if not disable buttons that would show transaction history or stats
    void checkIfTransactions() {

        Cursor cursorTemp = myDB.readAllData();

        if (cursorTemp.getCount() == 0) {
            budgetTimeFrame.setEnabled(false);
            spendingHistory.setEnabled(false);
        }
        else {
            budgetTimeFrame.setEnabled(true);
            spendingHistory.setEnabled(true);
        }

    }

    // checks if there is a budget value, if not force user to add new budget and disables everything else
    void checkIfBudget() {
        Cursor cursor = myOtherDB.readBudget();

        if (cursor.getCount() == 0) {
            addTransaction.setEnabled(false);
            budgetTimeFrame.setEnabled(false);
            spendingHistory.setEnabled(false);
            if (randomCount < 1)
                Toast.makeText(this,R.string.toast_set_budget, Toast.LENGTH_LONG).show();
            TVbudget.setText(R.string.zero_dollars);
            TVremaining.setText(R.string.zero_dollars);
            TVbudget.setTextColor(Color.BLACK);
            TVremaining.setTextColor(Color.BLACK);
            randomCount++;
        }
        else {
            addTransaction.setEnabled(true);
            randomCount = 0;

        }
    }

    // makes sure to call updating functions after coming back to home screen
    @Override
    protected void onResume() {
        super.onResume();
        updateRemaining();
        updateBudget();
        checkIfBudget();
        checkIfTransactions();
        checkTransactionCount();
        updateRecent();
        randomCount = 0;
    }

    public void checkTransactionCount () { // checks how many transactions there are and if its less than 3 it'll make the elements invisible depending on how many transactions there are

        Cursor cursor = myDB.readAllData();
        firstRecent = findViewById(R.id.BLOCK_mostRecentTransaction);
        secondRecent = findViewById(R.id.BLOCK_2ndMostRecentTransaction);
        thirdRecent = findViewById(R.id.BLOCK_3rdMostRecentTransaction);

        if (cursor.getCount() == 0) {
            firstRecent.setVisibility(View.INVISIBLE);
            secondRecent.setVisibility(View.INVISIBLE);
            thirdRecent.setVisibility(View.INVISIBLE);
        }
        else if (cursor.getCount() == 1) {
            firstRecent.setVisibility(View.VISIBLE);
            secondRecent.setVisibility(View.INVISIBLE);
            thirdRecent.setVisibility(View.INVISIBLE);
        }
        else if (cursor.getCount() == 2) {
            firstRecent.setVisibility(View.VISIBLE);
            secondRecent.setVisibility(View.VISIBLE);
            thirdRecent.setVisibility(View.INVISIBLE);
        }
        else  {
            firstRecent.setVisibility(View.VISIBLE);
            secondRecent.setVisibility(View.VISIBLE);
            thirdRecent.setVisibility(View.VISIBLE);
        }

    }

    // updates budget value
    public void updateBudget() {

        Cursor cursor = myOtherDB.readBudget();

        if (cursor.getCount() == 0 ) {
            return;
        }

        while (cursor.moveToNext()) {
            TVbudget.setText("$" + twoPlaces.format(cursor.getDouble(0)));
        }


    }

    // calculates remaining value by adding up all transactions and adding them to budget value
    public void updateRemaining() {

        Cursor cursor = myDB.readAllData();
        Cursor cursor2 = myOtherDB.readBudget();

        if (cursor2.getCount() == 0) {
            return;
        }

        cursor2.moveToFirst();
        double budget = cursor2.getDouble(0);
        double totalSpent = 0;

        while (cursor.moveToNext()) {
            totalSpent += cursor.getDouble(1);
        }

        double remaining = budget + totalSpent;


        TVremaining.setText("$" + twoPlaces.format(remaining));

        if (remaining < 0) {
            TVremaining.setTextColor(Color.parseColor("#DC143C"));
        }
        else if (remaining > 0) {
            TVremaining.setTextColor(Color.parseColor("#008000"));
        }
        else {
            TVremaining.setTextColor(Color.BLACK);
        }


    }

}