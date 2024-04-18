package com.example.finalandroidgit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.widget.Toast;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class SpendingHistoryScreen extends AppCompatActivity {

    RecyclerView recyclerView;

    MyDatabaseHelper myDB;
    ArrayList<String> transID, transAmount, transName, transMonth, transDay, transYear, transRemaining;
    CustomAdapter customAdapter;
    DecimalFormat twoPlaces = new DecimalFormat("0.00");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spending_history);

        recyclerView = findViewById(R.id.recyclerView);

        Button homeButton = findViewById(R.id.btnHomeButtonSpendingScreen);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        myDB = new MyDatabaseHelper(SpendingHistoryScreen.this);

        transID = new ArrayList<>();
        transAmount = new ArrayList<>();
        transName = new ArrayList<>();
        transMonth = new ArrayList<>();
        transDay = new ArrayList<>();
        transYear = new ArrayList<>();
        transRemaining = new ArrayList<>();

        storeDataInArrays();

        customAdapter = new CustomAdapter( SpendingHistoryScreen.this,SpendingHistoryScreen.this, transID, transAmount, transName, transMonth, transDay, transYear, transRemaining);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(SpendingHistoryScreen.this));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            recreate();
        }
    }

    // adds all data from database into arrays, recycler view will eventually read all the array data and display it
    void storeDataInArrays() {

        Cursor cursor = myDB.readAllData();
        int count = 1;

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
        }
        else {
            while (cursor.moveToNext()) {
                //transID.add(Integer.toString(count));
                transID.add(cursor.getString(0));
                transAmount.add(twoPlaces.format(cursor.getDouble(1)));
                transName.add(cursor.getString(2));
                transMonth.add(cursor.getString(3));
                transDay.add(cursor.getString(4));
                transYear.add(cursor.getString(5));
                transRemaining.add(twoPlaces.format(cursor.getDouble(6)));
                count++;
            }
        }

    }

}