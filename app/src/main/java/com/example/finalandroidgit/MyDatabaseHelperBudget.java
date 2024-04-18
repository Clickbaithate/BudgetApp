package com.example.finalandroidgit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyDatabaseHelperBudget extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "BudgetValue.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "my_budget_values";
    private static final String COLUMN_BUDGET = "budget_amount";
    private static final String COLUMN_REMAINING = "remaining_budget";

    public MyDatabaseHelperBudget(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    // creates database
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query =
                "CREATE TABLE " + TABLE_NAME +
                        " (" + COLUMN_BUDGET + " REAL);";
        db.execSQL(query);
    }

    // deletes database
    public void deleteData(SQLiteDatabase db) {
        String query = "DELETE FROM " + TABLE_NAME;
        db.execSQL(query);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int ii) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // budget value as parameter and adds that value to the database
    void addBudget(double budget) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_BUDGET, budget);

        long result = db.insert(TABLE_NAME, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
        }

    }

    // returns cursor that allows you to read budget
    Cursor readBudget() {
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if (db != null) {
            cursor = db.rawQuery(query, null);
        }

        return cursor;

    }




}