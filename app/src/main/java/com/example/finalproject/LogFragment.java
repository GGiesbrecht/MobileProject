package com.example.finalproject;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class LogFragment extends Fragment {
    private LogFragmentListener listener;
    private Spinner spHours, spMinutes;
    private Button btnLog, btnShow;
    private DatabaseHelper dbHelper;

    public interface LogFragmentListener {
        void sendToActivity(int hours, int minutes);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragmant_log, container, false);

        btnShow = (Button) v.findViewById(R.id.btnShow);
        btnLog = (Button) v.findViewById(R.id.btnLog);
        spHours = (Spinner) v.findViewById(R.id.spHours);
        spMinutes = (Spinner) v.findViewById(R.id.spMinutes);

        setupSpinners(v);

        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hours = Integer.parseInt(spHours.getSelectedItem().toString());
                int minutes = Integer.parseInt(spMinutes.getSelectedItem().toString());
                listener.sendToActivity(hours, minutes);
            }
        });

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showData();
            }
        });

        return v;
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    private void setupSpinners(View v) {
        ArrayList<String> hoursList = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            hoursList.add(String.valueOf(i));
        }

        ArrayList<String> minutesList = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            minutesList.add(String.valueOf(i));
        }

        ArrayAdapter<String> hoursAdapter = new ArrayAdapter<>(
            v.getContext(), R.layout.spinner, hoursList);
        ArrayAdapter<String> minutesAdapter = new ArrayAdapter<>(
            v.getContext(), R.layout.spinner, minutesList);

        hoursAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        minutesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spHours.setAdapter(hoursAdapter);
        spMinutes.setAdapter(minutesAdapter);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof LogFragmentListener) {
            listener = (LogFragmentListener) context;
            dbHelper = new DatabaseHelper(context);
        } else {
            throw new RuntimeException(context.toString() + " must implement LogFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
