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
    private final String BAR = "bar";
    private final String LINE = "line";
    private final String NIGHT_MODE = "nightMode";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor prefEditor;
    private Button btnAccept;
    private RadioButton radioBar, radioLine;
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
        radioBar = (RadioButton) findViewById(R.id.radioBar);
        radioLine = (RadioButton) findViewById(R.id.radioLine);
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

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setSelectedFeedType();
        setSwitchIsChecked();
    }

    private void setSwitchIsChecked() {
       swNight.setChecked(sharedPreferences.getBoolean(NIGHT_MODE, false));
    }

    private void setSelectedFeedType() {
        switch (sharedPreferences.getString("graphType", BAR)) {
            case LINE:
                radioLine.setChecked(true);
                break;
            default:
                radioBar.setChecked(true);
                break;
        }
    }

    public void radioButtonClick(View v) {
        boolean checked = ((RadioButton) v).isChecked();
        switch (v.getId()) {
            case R.id.radioBar:
                if (checked) {
                    prefEditor.putString("graphType", BAR);
                }
                break;
            case R.id.radioLine:
                if (checked) {
                    prefEditor.putString("graphType", LINE);
                }
                break;
        }
        prefEditor.commit();
    }
}
