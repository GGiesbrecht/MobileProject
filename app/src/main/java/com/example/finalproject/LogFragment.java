package com.example.finalproject;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class LogFragment extends Fragment {
    private LogFragmentListener listener;
    private NumberPicker numpHours, numpMinutes;
    private Button btnLog;

    public interface LogFragmentListener {
        void sendToActivity(int hours, int minutes);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragmant_log, container, false);

        btnLog = v.findViewById(R.id.btnLog);
        numpHours = v.findViewById(R.id.numHours);
        numpMinutes = v.findViewById(R.id.numMinutes);

        numpHours.setMinValue(0);
        numpHours.setMaxValue(24);
        numpMinutes.setMinValue(0);
        numpMinutes.setMaxValue(59);

        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hours = numpHours.getValue();
                int minutes = numpMinutes.getValue();
                listener.sendToActivity(hours, minutes);
            }
        });

        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof LogFragmentListener) {
            listener = (LogFragmentListener) context;
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
