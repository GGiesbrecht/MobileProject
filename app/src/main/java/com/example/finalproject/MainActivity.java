package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    EditText etUpdate;
    private NumberPicker numpHours, numpMinutes;
    private Button btnLog, btnShow, btnUpdate, btnDelete;
    BottomNavigationView bottomNav;
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
        btnLog = (Button) findViewById(R.id.btnLog);
        btnShow = (Button) findViewById(R.id.btnShow);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        numpHours = (NumberPicker) findViewById(R.id.numHours);
        numpMinutes = (NumberPicker) findViewById(R.id.numMinutes);
        etUpdate = (EditText) findViewById(R.id.etUpdate);
        bottomNav = (BottomNavigationView) findViewById(R.id.bottomNav);

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showData();
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData();
            }
        });

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                switch (item.getItemId()) {
                    case R.id.nav_log:
                        selectedFragment = new LogFragment();
                        break;
                    case R.id.nav_graph:
                        selectedFragment = new GraphFragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(
                    R.id.fragmentContainer, selectedFragment).commit();

                return true;
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
        boolean isInserted = dbHelper.insert(hours, minutes);
        if (isInserted) {
            Toast.makeText(MainActivity.this, "Data inserted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "Data not inserted", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateData() {
        dbHelper.update(
            Integer.parseInt(etUpdate.getText().toString()),
            numpHours.getValue(),
            numpMinutes.getValue());
    }

    private void deleteData() {
        dbHelper.delete(Integer.parseInt(etUpdate.getText().toString()));
    }

    private void showData() {
        Cursor cursor = dbHelper.getAllData();

        if (cursor.getCount() == 0) {
            showMessage("Error", "Nothing found");
            return;
        }

        StringBuffer buffer = new StringBuffer();
        while (cursor.moveToNext()) {
            buffer.append("ID: " + cursor.getString(0) + "\n");
            buffer.append("HOURS: " + cursor.getString(1) + "\n");
            buffer.append("MINUTES: " + cursor.getString(2) + "\n");
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
