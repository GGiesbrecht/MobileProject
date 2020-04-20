package com.example.finalproject;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Calendar;


import java.util.TimeZone;

public class DatabaseHelper extends SQLiteOpenHelper {
    private SQLiteDatabase db;

    private static final String DB_NAME = "productivity_tracker.db";
    private static final int DB_VERSION = 2;

    private static final String DAY = "day";
    private static final String GOAL = "goal";
    private static final String YEAR = "year";
    private static final String ID = "id";
    private static final String MINUTES = "minutes";
    private static final String GOAL_MINUTES = "goal_minutes";
    private static final String DAY_OF_YEAR = "day_of_year";
    private static final String DISPLAY_DATE = "display_date";

    private static final String CREATE_DAYS =
        "CREATE TABLE " + DAY + " (" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            MINUTES + " INTEGER, " +
            GOAL_MINUTES + " INTEGER, " +
            DISPLAY_DATE + " INTEGER, " +
            YEAR + " INTEGER, " +
            DAY_OF_YEAR + " INTEGER " +
            ");";

    private static final String DROP_DAYS = "DROP TABLE IF EXISTS " + DAY;
    private static final String DROP_GOALS = "DROP TABLE IF EXISTS " + GOAL;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        db = this.getWritableDatabase();
//        onCreate(this.getReadableDatabase());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DROP_DAYS);
        db.execSQL(CREATE_DAYS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    public boolean updateDay(int timeInMinutes, int goalInMinutes) {
        Calendar cal = DateUtil.getCalendar();
        int year = cal.get(Calendar.YEAR);
        int dayOfYear = cal.get(Calendar.DAY_OF_YEAR);

        ContentValues insertValues = new ContentValues();
        insertValues.put(MINUTES, timeInMinutes);
        insertValues.put(GOAL_MINUTES, goalInMinutes);
        insertValues.put(DISPLAY_DATE, System.currentTimeMillis());
        insertValues.put(YEAR, year);
        insertValues.put(DAY_OF_YEAR, dayOfYear);

        long result = db.update(DAY, insertValues, "year = ? AND day_of_year = ?",
            new String[]{String.valueOf(year), String.valueOf(dayOfYear)});

        return result != -1;
    }

    public void insertBlankDay() {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        long dateTime = System.currentTimeMillis();

        ContentValues insertValues = new ContentValues();
        insertValues.put(MINUTES, 0);
        insertValues.put(GOAL_MINUTES, 0);
        insertValues.put(DISPLAY_DATE, dateTime);
        insertValues.put(YEAR, cal.get(Calendar.YEAR));
        insertValues.put(DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR));

        db.insert(DAY, null, insertValues);
    }

    public boolean currentDayExists() {
        Calendar calendar = Calendar.getInstance();
        int currentDayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
        int currentYear = calendar.get(Calendar.YEAR);

        Cursor c = db.query(
            DAY,
            new String[]{YEAR, DAY_OF_YEAR},
            "year = " + currentYear + " AND " +
                "day_of_year = " + currentDayOfYear,
            null, null, null, null
        );

        boolean dayExists = false;
        if (c.getCount() != 0) {
            dayExists = true;
        }
        c.close();

        return dayExists;
    }

    public Cursor getAllData() {
        return db.rawQuery("SELECT * FROM " + DAY, null);
    }

    public Cursor getWeekData() {
        int currentYear = DateUtil.getCalendar().get(Calendar.YEAR);
        int mondayOfWeek = DateUtil.getMondayOfWeek().getDayOfYear();
        int sundayOfWeek = DateUtil.getSundayOfWeek().getDayOfYear();

        return db.rawQuery(
            "SELECT * FROM " + DAY +
                " WHERE year = ? " +
                " AND day_of_year >= ? " +
                " AND day_of_year <= ? ",
            new String[] {String.valueOf(currentYear), String.valueOf(mondayOfWeek),
                String.valueOf(sundayOfWeek)}
        );
    }

}
