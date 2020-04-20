package com.example.finalproject;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class GraphFragment extends Fragment {
    private TextView tvYear, tvMonth, tvWeek;
    private WebView webView;
    private DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragmant_graph, container, false);
        dbHelper = new DatabaseHelper(getActivity());

        assignViews(v);
        setTextViews();
        setupWebView();

        return v;
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

    @SuppressLint("SetJavaScriptEnabled")
    private void setupWebView() {
        WebSettings webViewSettings = webView.getSettings();
        webViewSettings.setJavaScriptEnabled(true);
        webViewSettings.setLoadWithOverviewMode(true);
        webViewSettings.setUseWideViewPort(true);
        webView.setWebViewClient(new WebViewClient());

        List<String> loggedHours = getLoggedHours();
        List<String> goalHours = getGoalHours();
        loadUrl(loggedHours, goalHours);
    }

    private List<String> getLoggedHours() {
        Cursor c = dbHelper.getWeekData();
        List<String> hoursList = new ArrayList<>(Arrays.asList("0", "0", "0", "0", "0", "0", "0"));

        LocalDate mondayOfWeek = DateUtil.getMondayOfWeek();

        while (c.moveToNext()) {
            String dayId = c.getString(0);
            String minutes = c.getString(1);
            String goalMinutes = c.getString(2);
            String dateString = c.getString(3);
            String year = c.getString(4);
            String day_of_year = c.getString(5);

            LocalDate date = Instant.ofEpochMilli(Long.parseLong(dateString)).atZone(ZoneId.systemDefault()).toLocalDate();

        }
    }

    private List<String> getGoalHours() {

    }

    private void loadUrl(List<String> loggedHours, List<String> goalHours) {
        String hoursString = android.text.TextUtils.join(",", loggedHours);
        String goalsString = android.text.TextUtils.join(",", goalHours);
        webView.loadUrl(
            "https://quickchart.io/chart?w=300&h=300&f=png&bkg=white&c={" +
                "type:%27bar%27,data:{" +
                    "labels:['Mon','Tue','Wed','Thu','Fri', 'Sat', 'Sun']," +
                    "datasets:[{" +
                        "label:%27Hours Worked%27," +
                        "data:[" + hoursString + "]" +
                    "}," +
                    "{" +
                        "label:%27Goal%27," +
                        "data:[" + goalsString + "]" +
                    "}" +
                "]" +
            "}" +
        "}"
        );
    }

}
