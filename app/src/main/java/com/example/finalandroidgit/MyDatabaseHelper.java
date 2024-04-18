package com.example.finalandroidgit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;
import androidx.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "Budget.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "my_budget";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_AMOUNT = "transaction_amount";
    private static final String COLUMN_NAME = "transaction_name";
    private static final String COLUMN_MONTH = "transaction_month";
    private static final String COLUMN_DAY = "transaction_day";
    private static final String COLUMN_YEAR = "transaction_year";
    private static final String COLUMN_REMAINING = "current_remaining_budget";

    MyDatabaseHelperBudget myDB;

    MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    // creates database
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query =
                "CREATE TABLE " + TABLE_NAME +
                        " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_AMOUNT + " REAL, " +
                        COLUMN_NAME + " TEXT, " +
                        COLUMN_MONTH + " INTEGER, " +
                        COLUMN_DAY + " INTEGER, " +
                        COLUMN_YEAR + " INTEGER, " +
                        COLUMN_REMAINING + " REAL);";
        db.execSQL(query);
    }

    // deletes data
    public void deleteData(SQLiteDatabase db) {
        String query = "DELETE FROM " + TABLE_NAME;
        db.execSQL(query);

        db.delete("SQLITE_SEQUENCE","NAME = ?",new String[]{TABLE_NAME});
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int ii) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // takes values as parameters and adds those values to the database
    void addTransaction(double amount, String name, int month, int day, int year) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        myDB = new MyDatabaseHelperBudget(context);

        Cursor cursorTHIS = readAllData();
        double totalSpent = 0;

        if (cursorTHIS.getCount() != 0) {
            while (cursorTHIS.moveToNext()) {
                totalSpent += cursorTHIS.getDouble(1);
            }
        }

        Cursor cursor = myDB.readBudget();
        double currentBudget = 0;
        if (cursor.moveToFirst()) {
            currentBudget = cursor.getDouble(0);
        }

        double remaining = currentBudget+(totalSpent+amount);


        cv.put(COLUMN_AMOUNT, (1*amount));
        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_MONTH, month);
        cv.put(COLUMN_DAY, day);
        cv.put(COLUMN_YEAR, year);
        cv.put(COLUMN_REMAINING, remaining);


        long result = db.insert(TABLE_NAME, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
        }

    }

    // returns cursor that reads data from database
    Cursor readAllData() {
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if (db != null) {
            cursor = db.rawQuery(query, null);
        }

        return cursor;

    }

    // returns cursor that reads data from database from bottom to top
    Cursor rearrangeDatabase() {

        String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY ROWID DESC";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if (db != null) {
            cursor = db.rawQuery(query, null);
        }

        return cursor;

    }

    // updates values of an already established transaction
    void updateTransaction(String row_id, double amount, String name, int month, int day, int year, double remaining) {

        SQLiteDatabase dbTHIS = this.getWritableDatabase();
        myDB = new MyDatabaseHelperBudget(context);



        ContentValues cv = new ContentValues();
        cv.put(COLUMN_AMOUNT, amount);
        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_DAY, day);
        cv.put(COLUMN_MONTH, month);
        cv.put(COLUMN_YEAR, year);
        cv.put(COLUMN_REMAINING, remaining);

        long result = dbTHIS.update(TABLE_NAME, cv, "_id=?", new String[]{row_id});


    }

}