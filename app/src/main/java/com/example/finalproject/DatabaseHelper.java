package com.example.finalproject;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import java.util.TimeZone;

public class DatabaseHelper extends SQLiteOpenHelper {
    SQLiteDatabase db;

    private static final String DB_NAME="productivity_tracker.db";
    private static final int DB_VERSION = 2;

    private static final String DAY = "day";
    private static final String GOAL = "goal";
    private static final String WEEK = "week";
    private static final String YEAR = "year";

    private static final String ID="id";
    private static final String HOURS = "hours";
    private static final String MINUTES = "minutes";
    private static final String GOAL_MINUTES = "goal_minutes";
    private static final String DAY_OF_WEEK = "day_of_week";
    private static final String WEEK_ID = "week_id";
    private static final String START_DATE = "start_date";
    private static final String END_DATE = "end_date";
    private static final String DAY_OF_YEAR = "day_of_year";
    private static final String GOAL_ID = "goal_id";
    private static final String DAY_ID = "day_id";

    private static final String REACHED = "reached";
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

//    private static final String CREATE_GOALS =
//        "CREATE TABLE " + GOAL + " (" +
//            ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//            MINUTES + " INTEGER, " +
////            REACHED + " INTEGER DEFAULT 0, " +
//            DAY_ID + "INTEGER UNIQUE NOT NULL, " +
//            "FOREIGN KEY("+ DAY_ID +") REFERENCES day(id)" +
//        ");";

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
        db.execSQL(DROP_GOALS);
        db.execSQL(CREATE_DAYS);
//        db.execSQL(CREATE_GOALS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        onCreate(db);
    }

    public boolean insertUpdateDay(int timeInMinutes, long dateTime) {
        Date date = stringToDate(String.valueOf(dateTime));
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        cal.setTime(date);

        int year = cal.get(Calendar.YEAR);
        int dayOfYear = cal.get(Calendar.DAY_OF_YEAR);

        ContentValues insertValues = new ContentValues();
        insertValues.put(MINUTES, timeInMinutes);
        insertValues.put(DISPLAY_DATE, dateTime);
        insertValues.put(YEAR, year);
        insertValues.put(DAY_OF_YEAR, dayOfYear);

        long result = -1;
        if (currentDayExists()) {
            result = db.update(DAY, insertValues, "year = ? AND day_of_year = ?",
                new String[] { String.valueOf(year), String.valueOf(dayOfYear) });
        } else {
            result = db.insert(DAY, null, insertValues);
        }
        return result != -1;
    }

//    public boolean insertUpdateGoal(int goalInMinutes) {
////        Cursor c = db.query(GOAL, new String[] {ID}, getGoalQuery(),
////            null,null,null,null);
////
////        ContentValues insertValues = new ContentValues();
////        insertValues.put(MINUTES, goalInMinutes);
////        insertValues.put(DAY_ID, dayId);
////
////        if (currentGoalExists()) {
////            db.update(GOAL, insertValues);
////        } else {
////            db.insert();
////        }
////
////        long result = db.insert(GOAL, null, insertValues);
////        return result != -1;
////    }

    public boolean currentDayExists() {
        boolean dayExists = false;
        Calendar calendar = Calendar.getInstance();
        int currentDayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
        int currentYear = calendar.get(Calendar.YEAR);

        String[] selection = {YEAR, DAY_OF_YEAR};
        Cursor c = db.query(
            DAY,
            selection,
            "year = " + currentYear + " AND " +
            "day_of_year = " + currentDayOfYear,
            null, null, null, null
        );
        c.close();

        if (c.getCount() > 0) {
            dayExists = true;
        }
        return dayExists;
    }

    public boolean currentGoalExists() {
        boolean goalExists = false;

        String[] selection = {MINUTES, REACHED};
        Cursor c = db.query(
            GOAL,
            selection,
            getGoalQuery(),
            null, null, null, null
        );
        c.close();

        if (c.getCount() > 0) {
            goalExists = true;
        }
        return goalExists;
    }

    private String getGoalQuery() {
        Calendar calendar = Calendar.getInstance();
        int currentDayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
        int currentYear = calendar.get(Calendar.YEAR);

        return "day_id = (SELECT id " +
            "              FROM day" +
            "              WHERE year = " + currentYear +
            "              AND day_of_year = " + currentDayOfYear + ")";
    }

    public Cursor getDayData() {
        return db.rawQuery(
            "SELECT * FROM " + DAY, null);
    }

    public Cursor getGoalData() {
        return db.rawQuery(
            "SELECT * FROM " + GOAL, null);
    }

    public Date stringToDate(String string) {
        DateFormat format = DateFormat.getDateInstance();
        Date date = new Date();

        try {
            date = format.parse(string);
        } catch (Exception e) {
            Log.e("Date parse error", e.toString());
        }

        return date;
    }

//    public boolean update(int id, int hours, int minutes) {
//        ContentValues insertValues = new ContentValues();
//        insertValues.put(ID, id);
//        insertValues.put(HOURS, hours);
//        insertValues.put(MINUTES, minutes);
//
//        db.update(DAY, insertValues, "ID = ?", new String[] { String.valueOf(id) });
//        return true;
//    }

//    public Integer delete(int id) {
//        return db.delete(DAY, "ID = ?", new String[] { String.valueOf(id) });
//    }

//    public void saveExec(String name, int age)
//    {
//        String insertStatement = "INSERT INTO 'People' VALUES('" + name +"'," + age + ");";
//
//        db.execSQL(insertStatement);
//        db.close();
//    }

    //Load the data in the table
//    public ArrayList<String> loadData(){
//
//        ArrayList<String> nameData = new ArrayList<String>();
//        //open the readable database
////        SQLiteDatabase db = this.getReadableDatabase();
//        //create an array of the table names
//        String[] selection = {HOURS, MINUTES};
//        //Create a cursor item for querying the database
//        Cursor c = db.query(DAY,	//The name of the table to query
//            selection,				//The columns to return
//            null,					//The columns for the where clause
//            null,					//The values for the where clause
//            null,					//Group the rows
//            null,					//Filter the row groups
//            null);					//The sort order
//
//
//
//        //Move to the first row
//        c.moveToFirst();
//
//        //For each row that was retrieved
//        for(int i=0; i < c.getCount(); i++)
//        {
//            //assign the value to the corresponding array
//            nameData.add(c.getString(0));
//            c.moveToNext();
//        }
//
//        //close the cursor
//        c.close();
//        //close the database
//        db.close();
//
//        return nameData;
//    }



//    public List<Map<String,String>> loadData2()
//    {
//        List<Map<String,String>> lm = new ArrayList<Map<String,String>>();
//
//        //open the readable database
////        SQLiteDatabase db = this.getReadableDatabase();
//        //create an array of the table names
//        String[] selection = {HOURS, MINUTES};
//        //Create a cursor item for querying the database
//        Cursor c = db.query(DAY,	//The name of the table to query
//            selection,				//The columns to return
//            null,					//The columns for the where clause
//            null,					//The values for the where clause
//            null,					//Group the rows
//            null,					//Filter the row groups
//            null);					//The sort order
//
//
//
//        //Move to the first row
//        c.moveToFirst();
//
//        //For each row that was retrieved
//        for(int i=0; i < c.getCount(); i++)
//        {
//            Map<String,String> map = new HashMap<String,String>();
//            //assign the value to the corresponding array
//            map.put("Name", c.getString(0));
//            map.put("Age", String.valueOf(c.getInt(1)));
//
//            lm.add(map);
//            c.moveToNext();
//        }
//
//        //close the cursor
//        c.close();
//        //close the database
//        db.close();
//        return lm;
//
//    }

}
