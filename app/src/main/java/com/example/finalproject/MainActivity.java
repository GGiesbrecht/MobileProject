package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements LogFragment.LogFragmentListener {
    Toolbar toolbar;
    BottomNavigationView bottomNav;
    DatabaseHelper dbHelper;
    Boolean curFragIsLog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        curFragIsLog = true;

        toolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);

        dbHelper = new DatabaseHelper(this);
        setupBottomNav();

        getSupportFragmentManager().beginTransaction()
            .replace(R.id.fragmentContainer, new LogFragment()).commit();
    }


    @Override
    public void sendToActivity(int hours, int minutes) {
        int timeInMinutes = (hours * 60) + minutes;

        logTime(timeInMinutes);
    }

    public void logTime(int timeInMinutes) {
        boolean isInserted = dbHelper.insert(timeInMinutes, System.currentTimeMillis());

        Toast.makeText(MainActivity.this, isInserted
                ? "Data inserted"
                : "Data not inserted",
            Toast.LENGTH_SHORT).show();
    }

    private void setupBottomNav() {
        bottomNav = (BottomNavigationView) findViewById(R.id.bottomNav);

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_log:
                        if (!curFragIsLog) {
                            setFragmentLog();
                            curFragIsLog = true;
                        }
                        break;
                    case R.id.nav_graph:
                        if (curFragIsLog) {
                            setFragmentGraph();
                            curFragIsLog = false;
                        }
                        break;
                }
                return true;
            }
        });
    }

    private void setFragmentLog() {
        getSupportFragmentManager()
            .beginTransaction()
            .setCustomAnimations(R.anim.enter_left_to_right, R.anim.exit_right_to_left)
            .replace(R.id.fragmentContainer, new LogFragment())
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit();
    }

    private void setFragmentGraph() {
        getSupportFragmentManager()
            .beginTransaction()
            .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_left_to_right)
            .replace(R.id.fragmentContainer, new GraphFragment())
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit();
    }

    private void showData() {
        Cursor cursor = dbHelper.getWeekData();

        if (cursor.getCount() == 0) {
            showMessage("Error", "Nothing found");
            return;
        }

        StringBuffer buffer = new StringBuffer();
        while (cursor.moveToNext()) {
            buffer.append("ID: " + cursor.getString(0) + "\n");
            buffer.append("Hours: " + cursor.getString(1) + "\n");
            buffer.append("Minutes: " + cursor.getString(2) + "\n");
            buffer.append("Date: " + cursor.getString(3) + "\n");
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
