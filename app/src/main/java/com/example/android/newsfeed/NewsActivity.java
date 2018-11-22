package com.example.android.newsfeed;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class NewsActivity extends AppCompatActivity {

    // Define URL used to query The Guardian's API
    // todo define is as static
    private final String NEWS_QUERY_URL = "https://content.guardianapis.com/technology/2014/feb/18/doge-such-questions-very-answered?api-key=test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
    }
}
