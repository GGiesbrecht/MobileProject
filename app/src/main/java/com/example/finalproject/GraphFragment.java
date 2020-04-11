package com.example.finalproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class GraphFragment extends Fragment {
    private WebView webView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragmant_graph, container, false);
        webView = v.findViewById(R.id.wvChart);

        WebSettings webViewSettings = webView.getSettings();
        webViewSettings.setJavaScriptEnabled(true);
        webViewSettings.setLoadWithOverviewMode(true);
        webViewSettings.setUseWideViewPort(true);

        webView.setWebViewClient(new WebViewClient());
        loadUrl();

        return v;
    }

    private void loadUrl() {
        webView.loadUrl("https://quickchart.io/chart?w=300&h=300&f=png&bkg=white&c={" +
            "type:%27bar%27,data:{" +
                "labels:['Mon','Tue','Wed','Thu','Fri']," +
                "datasets:[{" +
                "label:%27Hours Worked%27," +
                "data:[1,2, ,3,4,5,6,7,8]" +
                "}" +
                "]" +
            "}" +
        "}"
        );
    }
}
