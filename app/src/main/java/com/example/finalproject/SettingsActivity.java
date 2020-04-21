package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {
    private final String BLUE = "33d6ff";
    private final String GREEN = "00ff00";
    private final String GOLD = "CDAA35";
    private final String PURPLE = "cc66ff";
    private final String NIGHT_MODE = "nightMode";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor prefEditor;
    private Button btnAccept;
    private RadioButton radioBlue, radioGreen, radioGold, radioPurple;
    private Switch swNight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);
        prefEditor = sharedPreferences.edit();

        assignControls();
    }

    private void assignControls() {
        btnAccept = (Button) findViewById(R.id.btnAccept);
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        radioBlue = (RadioButton) findViewById(R.id.radBlue);
        radioGreen = (RadioButton) findViewById(R.id.radGreen);
        radioGold = (RadioButton) findViewById(R.id.radGold);
        radioPurple = (RadioButton) findViewById(R.id.radPurple);

        swNight = (Switch) findViewById(R.id.swNight);
        swNight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    prefEditor.putBoolean(NIGHT_MODE, true);
                } else {
                    prefEditor.putBoolean(NIGHT_MODE, false);
                }
                prefEditor.commit();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setSelectedColours();
        setSwitchIsChecked();
    }

    private void setSwitchIsChecked() {
       swNight.setChecked(sharedPreferences.getBoolean(NIGHT_MODE, false));
    }

    private void setSelectedColours() {
        switch (sharedPreferences.getString("hourColour", BLUE)) {
            case GREEN:
                radioGreen.setChecked(true);
                break;
            default:
                radioBlue.setChecked(true);
                break;
        }

        switch (sharedPreferences.getString("goalColour", GOLD)) {
            case PURPLE:
                radioPurple.setChecked(true);
                break;
            default:
                radioGold.setChecked(true);
                break;
        }
    }

    public void hoursBarColour(View v) {
        boolean checked = ((RadioButton) v).isChecked();
        switch (v.getId()) {
            case R.id.radBlue:
                if (checked) {
                    prefEditor.putString("hourColour", BLUE);
                }
                break;
            case R.id.radGreen:
                if (checked) {
                    prefEditor.putString("hourColour", GREEN);
                }
                break;
        }
        prefEditor.commit();
    }
    public void goalsBarColour(View v) {
        boolean checked = ((RadioButton) v).isChecked();
        switch (v.getId()) {
            case R.id.radGold:
                if (checked) {
                    prefEditor.putString("goalColour", GOLD);
                }
                break;
            case R.id.radPurple:
                if (checked) {
                    prefEditor.putString("goalColour", PURPLE);
                }
                break;
        }
        prefEditor.commit();
    }

}
