package com.example.finalproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private NumberPicker numpHours, numpMinutes;
    private Button btnLog, btnShow, btnUpdate;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);

        dbHelper = new DatabaseHelper(this);
        this.setupControls();
    }

    private void setupControls() {
        btnLog = (Button)findViewById(R.id.btnLog);
        btnShow = (Button)findViewById(R.id.btnShow);
        btnUpdate = (Button)findViewById(R.id.btnUpdate);
        numpHours = (NumberPicker)findViewById(R.id.numHours);
        numpMinutes = (NumberPicker)findViewById(R.id.numMinutes);

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showData();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean updateSuccess = dbHelper.updateData(numpHours.getValue(), numpMinutes.getValue());
            }
        });

        numpHours.setMinValue(0);
        numpHours.setMaxValue(24);
        numpMinutes.setMinValue(0);
        numpMinutes.setMaxValue(59);
    }

    public void logTime(View v) {
        int hours = numpHours.getValue();
        int minutes = numpMinutes.getValue();
        boolean isInserted = dbHelper.insertValues(hours, minutes);
        if (isInserted) {
            Toast.makeText(MainActivity.this, "Data inserted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "Data not inserted", Toast.LENGTH_SHORT).show();
        }
    }

    public void showData() {
        Cursor cursor = dbHelper.getAllData();

        if (cursor.getCount() == 0) {
            showMessage("Error", "Nothing found");
            return;
        }

        StringBuffer buffer = new StringBuffer();
        while (cursor.moveToNext()) {
            buffer.append("HOURS : " + cursor.getString(0) + "\n");
            buffer.append("MINUTES : " + cursor.getString(1) + "\n");
        }

        showMessage("Data", buffer.toString());
    }

    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.appbar, menu);
        return true;
    }
}
