package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements LogFragment.LogFragmentListener {
    private final int STANDARD_REQUEST_CODE = 222;
    public SharedPreferences sharedPreferences;

    RelativeLayout mainLayout;
    Toolbar toolbar;
    BottomNavigationView bottomNav;
    DatabaseHelper dbHelper;
    Boolean curFragIsLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        curFragIsLog = true;
        dbHelper = new DatabaseHelper(this);
        sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);
        mainLayout = findViewById(R.id.mainLayout);

        toolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);

        getSupportFragmentManager().beginTransaction()
            .replace(R.id.fragmentContainer, new LogFragment()).commit();

        setupBottomNav();
        initializeDay();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);
    }

    private void initializeDay() {
        if (!dbHelper.currentDayExists()) {
            dbHelper.insertBlankDay();
        }
    }

    @Override
    public void sendHoursToActivity(int hours, int minutes, int goalHours) {
        int timeInMinutes = (hours * 60) + minutes;
        int goalInMinutes = goalHours * 60;

        logDay(timeInMinutes, goalInMinutes);
    }

    public void logDay(int timeInMinutes, int goalInMinutes) {
        boolean isInserted = dbHelper.updateDay(timeInMinutes, goalInMinutes);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.appbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivityForResult(intent, STANDARD_REQUEST_CODE);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
