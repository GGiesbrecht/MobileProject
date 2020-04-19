package com.example.finalproject;

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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class GraphFragment extends Fragment {
    private TextView tvYear, tvMonth, tvWeek;
    private WebView webView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragmant_graph, container, false);
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

    private void setupWebView() {
        WebSettings webViewSettings = webView.getSettings();
        webViewSettings.setJavaScriptEnabled(true);
        webViewSettings.setLoadWithOverviewMode(true);
        webViewSettings.setUseWideViewPort(true);
        webView.setWebViewClient(new WebViewClient());
//        loadUrl();
    }

    private void setTextViews() {
        String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        String month = getMonthString(Calendar.getInstance().get(Calendar.MONTH));
        int weekStart = getMonday();
        int weekEnd = getSunday();

        tvMonth.setText(month);
        tvYear.setText(year);
        tvWeek.setText(String.format(Locale.getDefault(), "%02d - %02d", weekStart, weekEnd));
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

    private int getMonday() {
        LocalDate today = LocalDate.now();
        LocalDate monday = today;

        while (monday.getDayOfWeek() != DayOfWeek.MONDAY) {
            monday = monday.minusDays(1);
        }

        return monday.getDayOfMonth();
    }

    private int getSunday() {
        LocalDate today = LocalDate.now();
        LocalDate sunday = today;

        while (sunday.getDayOfWeek() != DayOfWeek.SUNDAY) {
            sunday = sunday.plusDays(1);
        }

        return sunday.getDayOfMonth();
    }

    private String getMonthString(int month) {
        String monthString = "";
        switch (month) {
            case 0:
                monthString = "January";
                break;
            case 1:
                monthString = "February";
                break;
            case 2:
                monthString = "March";
                break;
            case 3:
                monthString = "April";
                break;
            case 4:
                monthString = "May";
                break;
            case 5:
                monthString = "June";
                break;
            case 6:
                monthString = "July";
                break;
            case 7:
                monthString = "August";
                break;
            case 8:
                monthString = "September";
                break;
            case 9:
                monthString = "October";
                break;
            case 10:
                monthString = "November";
                break;
            case 11:
                monthString = "December";
                break;
        }
        return monthString;
    }
}
