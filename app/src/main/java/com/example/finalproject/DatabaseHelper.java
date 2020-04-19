package com.example.finalproject;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper {

    // DB name
    private static final String DB_NAME="productivity_tracker.db";
    private static final int DB_VERSION = 2;

    // Table definitions
    private static final String DAY = "day";
    private static final String WEEK = "week";
    private static final String YEAR = "year";

    // Columns
    private static final String ID="id";
    private static final String HOURS = "hours";
    private static final String MINUTES = "minutes";
    private static final String DAY_OF_WEEK = "day_of_week";
    private static final String WEEK_ID = "week_id";
    private static final String START_DATE = "start_date";
    private static final String END_DATE = "end_date";
    private static final String REACHED = "reached";


    private static final String DATE = "date";


    // Create table DAY
    private static final String CREATE_DAYS =
        "CREATE TABLE " + DAY + " (" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            MINUTES + " INTEGER, " +
            DATE + " INTEGER, " +
            "goalId INTEGER, " +
            "FOREIGN KEY(goalId) REFERENCES goal(id)" +
        ");";

    private static final String CREATE_GOALS =
        "CREATE TABLE goal (" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            MINUTES + " INTEGER, " +
            REACHED + " BOOLEAN, " +
            "dayId INTEGER, " +
            "FOREIGN KEY(dayId) REFERENCES day(id)" +
            ");";


    // Create table WEEK
//    private static final String CREATE_WEEKS =
//        "CREATE TABLE " + WEEK + " (" +
//            ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//            YEAR + " INTEGER, " +
//            START_DATE + " INTEGER, " +
//            END_DATE + " INTEGER" +
//            ");";

//    private static final String CREATE_YEARS =
//        "CREATE TABLE " + YEAR + " (" +
//            ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//            YEAR + "INTEGER " +
//            ");";

    //Drop table statements
    private static final String DROP_DAYS = "DROP TABLE IF EXISTS " + DAY;
    private static final String DROP_YEARS = "DROP TABLE IF EXISTS " + YEAR;
    private static final String DROP_WEEKS = "DROP TABLE IF EXISTS " + WEEK;

    //constructor
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        // TODO Auto-generated constructor stub
    }

    //when you create the class, create the table
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DROP_DAYS);
        db.execSQL(DROP_WEEKS);
//        db.execSQL(DROP_YEARS);
//        db.execSQL(DROP_MONTHS);

        db.execSQL(CREATE_DAYS);
        db.execSQL(CREATE_GOALS);
//        db.execSQL(CREATE_WEEKS);
//        db.execSQL(CREATE_YEARS);
//        db.execSQL(CREATE_TABLE_MONTHS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        //drop the table and recreate it
        onCreate(db);
    }

    //Insert values using content values
    public boolean insert(int timeInMinutes, long dateTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues insertValues = new ContentValues();

        insertValues.put(MINUTES_WORKED, timeInMinutes);
        insertValues.put(DATE, dateTime);

        long result = db.insert(DAY, null, insertValues);
        return result != -1;
    }

    public boolean update(int id, int hours, int minutes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues insertValues = new ContentValues();
        insertValues.put(ID, id);
        insertValues.put(HOURS, hours);
        insertValues.put(MINUTES, minutes);

        db.update(DAY, insertValues, "ID = ?", new String[] { String.valueOf(id) });
        return true;
    }

    public Integer delete(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(DAY, "ID = ?", new String[] { String.valueOf(id) });
    }

    public void saveExec(String name, int age)
    {
        String insertStatement = "INSERT INTO 'People' VALUES('" + name +"'," + age + ");";
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL(insertStatement);
        db.close();
    }

    //Load the data in the table
    public ArrayList<String> loadData(){

        ArrayList<String> nameData = new ArrayList<String>();
        //open the readable database
        SQLiteDatabase db = this.getReadableDatabase();
        //create an array of the table names
        String[] selection = {HOURS, MINUTES};
        //Create a cursor item for querying the database
        Cursor c = db.query(DAY,	//The name of the table to query
            selection,				//The columns to return
            null,					//The columns for the where clause
            null,					//The values for the where clause
            null,					//Group the rows
            null,					//Filter the row groups
            null);					//The sort order



        //Move to the first row
        c.moveToFirst();

        //For each row that was retrieved
        for(int i=0; i < c.getCount(); i++)
        {
            //assign the value to the corresponding array
            nameData.add(c.getString(0));
            c.moveToNext();
        }

        //close the cursor
        c.close();
        //close the database
        db.close();

        return nameData;
    }

    public Cursor getWeekData() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery(
            "SELECT * FROM " + DAY, null);
    }

    //This method is used to load the data from a table into a hash map
    public List<Map<String,String>> loadData2()
    {
        List<Map<String,String>> lm = new ArrayList<Map<String,String>>();

        //open the readable database
        SQLiteDatabase db = this.getReadableDatabase();
        //create an array of the table names
        String[] selection = {HOURS, MINUTES};
        //Create a cursor item for querying the database
        Cursor c = db.query(DAY,	//The name of the table to query
            selection,				//The columns to return
            null,					//The columns for the where clause
            null,					//The values for the where clause
            null,					//Group the rows
            null,					//Filter the row groups
            null);					//The sort order



        //Move to the first row
        c.moveToFirst();

        //For each row that was retrieved
        for(int i=0; i < c.getCount(); i++)
        {
            Map<String,String> map = new HashMap<String,String>();
            //assign the value to the corresponding array
            map.put("Name", c.getString(0));
            map.put("Age", String.valueOf(c.getInt(1)));

            lm.add(map);
            c.moveToNext();
        }

        //close the cursor
        c.close();
        //close the database
        db.close();
        return lm;

    }

}
