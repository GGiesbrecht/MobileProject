package com.example.finalproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class GraphFragment extends Fragment {
    private TextView tvYear, tvMonth, tvWeek;
    private WebView webView;
    private DatabaseHelper dbHelper;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragmant_graph, container, false);
        dbHelper = new DatabaseHelper(getActivity());

        assignViews(v);
        setTextViews();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        sharedPreferences = getActivity().getSharedPreferences("settings", Context.MODE_PRIVATE);
        setupWebView();
    }

    private void assignViews(View v) {
        webView = v.findViewById(R.id.wvChart);
        tvYear = v.findViewById(R.id.tvYear);
        tvMonth = v.findViewById(R.id.tvMonth);
        tvWeek = v.findViewById(R.id.tvWeek);
    }

    private void setTextViews() {
        String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        String month = DateUtil.getMonthString(Calendar.getInstance().get(Calendar.MONTH));
        int weekStart = DateUtil.getMondayOfWeek().getDayOfMonth();
        int weekEnd = DateUtil.getSundayOfWeek().getDayOfMonth();

        tvMonth.setText(month);
        tvYear.setText(year);
        tvWeek.setText(String.format(Locale.getDefault(), "%02d - %02d", weekStart, weekEnd));
    }

    private void setupWebView() {
        WebSettings webViewSettings = webView.getSettings();
        webViewSettings.setJavaScriptEnabled(true);
        webViewSettings.setLoadWithOverviewMode(true);
        webViewSettings.setUseWideViewPort(true);
        webView.setWebViewClient(new WebViewClient());

        Map<String,List<String>> map = getHoursMap();
        List<String> loggedHours = map.get("hoursList");
        List<String> goalHours = map.get("goalsList");

        loadUrl(loggedHours, goalHours);
    }

    private Map<String,List<String>> getHoursMap() {
        List<String> hoursList = new ArrayList<>(Arrays.asList("0", "0", "0", "0", "0", "0", "0"));
        List<String> goalsList = new ArrayList<>(Arrays.asList("0", "0", "0", "0", "0", "0", "0"));
        LocalDate monday = DateUtil.getMondayOfWeek();

        Cursor c = dbHelper.getWeekData();
        while (c.moveToNext()) {
            String minutes = c.getString(1);
            String goalMinutes = c.getString(2);
            String dateString = c.getString(3);

            double hours = Double.parseDouble(minutes) / 60;
            double goalHours = Double.parseDouble(goalMinutes) / 60;
            LocalDate date = Instant.ofEpochMilli(Long.parseLong(dateString))
                .atZone(ZoneId.systemDefault()).toLocalDate();

            for (int i = 0; i < 7; i++) {
                if (date.getDayOfYear() == monday.getDayOfYear() + i) {
                    hoursList.set(i, String.valueOf(hours));
                    goalsList.set(i, String.valueOf(goalHours));
                }
            }
        }

        Map<String,List<String>> map = new HashMap<>();
        map.put("hoursList", hoursList);
        map.put("goalsList", goalsList);
        return map;
    }

    private void loadUrl(List<String> loggedHours, List<String> goalHours) {
        String hoursList = android.text.TextUtils.join(",", loggedHours);
        String goalsList = android.text.TextUtils.join(",", goalHours);
//        sharedPreferences = getActivity().getSharedPreferences("settings", Context.MODE_PRIVATE);
        String hourColour = sharedPreferences.getString("hourColour", "33d6ff");
        String goalColour = sharedPreferences.getString("goalColour", "CDAA35");
        boolean nightMode = sharedPreferences.getBoolean("nightMode", false);
        String backgroundColour = nightMode ? "black" : "white";


        webView.loadUrl(
            "https://quickchart.io/chart?w=300&h=300&f=png&bkg="+ backgroundColour +"&c={" +
                "type:%27bar%27,data:{" +
                    "labels:['Mon','Tue','Wed','Thu','Fri', 'Sat', 'Sun']," +
                    "datasets:[{" +
                        "label:%27Hours Worked%27," +
                        "data:["+ hoursList +"]," +
                        "backgroundColor:%27%23"+ hourColour +"%27," +
                    "}," +
                    "{" +
                        "label:%27Goal%27," +
                        "data:["+ goalsList +"]," +
                        "backgroundColor:%27%23"+ goalColour +"%27," +
                    "}" +
                "]" +
            "}" +
        "}"
        );
    }

}
