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

    //Define your database name
    private static final String DB_NAME="productivity_tracker.db";
    private static final String TABLE_NAME="day_table";

    //Create constants defining your column names
    private static final String COL_ID="ID";
    private static final String COL_HOURS="HOURS";
    private static final String COL_MINUTES="MINUTES";
    //Define the database version
    private static final int DB_VERSION = 2;

    //Define your create statement in typical sql format
    //CREATE TABLE {Tablename} (
    //Colname coltype
    //)
    private static final String TABLE_CREATE =
        "CREATE TABLE " + TABLE_NAME + " (" +
            COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_HOURS + " INTEGER, " +
            COL_MINUTES + " INTEGER);";

    //Drop table statement
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    //constructor
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        // TODO Auto-generated constructor stub
    }

    //when you create the class, create the table
    @Override
    public void onCreate(SQLiteDatabase db) {
        // execute the create table code
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        //drop the table and recreate it
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

    //Insert values using content values
    public boolean insertValues(int hours, int minutes) {
        //get an instance of a writable database
        SQLiteDatabase db = this.getWritableDatabase();

        //create an instance of ContentValues to add to the database
        //the ContentValues class is used to store sets of values that
        //are easier to process
        ContentValues insertValues = new ContentValues();
        //Add values to the ContentValues:
        //insertValues.put(ColumnName, value);
        insertValues.put(COL_HOURS, hours);
        insertValues.put(COL_MINUTES, minutes);
        //insert the values into the table
        long result = db.insert(TABLE_NAME, null, insertValues);

        //close the database
        db.close();

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean updateData(int id, int hours, int minutes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues insertValues = new ContentValues();
        insertValues.put(COL_ID, id);
        insertValues.put(COL_HOURS, hours);
        insertValues.put(COL_MINUTES, minutes);

        db.update(TABLE_NAME, insertValues, "id = ?", new String[] { String.valueOf(id) });
        return true;
    }

    public void saveExec(String name, int age)
    {
        //Open your writable database
        SQLiteDatabase db = this.getWritableDatabase();

        //Formulate your statement
        String insertStatement = "INSERT INTO 'People' VALUES('" + name +"'," + age + ");";

        //Execute your statement
        db.execSQL(insertStatement);

        db.close();

    }

    //Load the data in the table
    public ArrayList<String> loadData(){

        ArrayList<String> nameData = new ArrayList<String>();
        //open the readable database
        SQLiteDatabase db = this.getReadableDatabase();
        //create an array of the table names
        String[] selection = {COL_HOURS, COL_MINUTES};
        //Create a cursor item for querying the database
        Cursor c = db.query(TABLE_NAME,	//The name of the table to query
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

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return cursor;
    }

    //This method is used to load the data from the table into a hash map
    //this enables the use of multiple textviews in the listview
    public List<Map<String,String>> loadData2()
    {
        List<Map<String,String>> lm = new ArrayList<Map<String,String>>();

        //open the readable database
        SQLiteDatabase db = this.getReadableDatabase();
        //create an array of the table names
        String[] selection = {COL_HOURS, COL_MINUTES};
        //Create a cursor item for querying the database
        Cursor c = db.query(TABLE_NAME,	//The name of the table to query
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
