package com.example.rus.exercise2_2.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;

import com.example.rus.exercise2_2.AboutActivity;
import com.example.rus.exercise2_2.R;

public class NYNewsDetailsActivity extends AppCompatActivity {

    private static final String URL_EXTRA = "url";

    public static Intent newIntent(Context context, String url) {
        Intent intent = new Intent(context, NYNewsDetailsActivity.class);
        intent.putExtra(URL_EXTRA, url);
        return intent;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.news_list_activity_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.about:
                goToAboutActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nynews_details);
        init();
    }

    private void init() {
        Toolbar toolbar = findViewById(R.id.nynews_details_activity_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        //after pr recommendation
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }


        WebView webView = findViewById(R.id.webview);
        webView.loadUrl(getIntent().getStringExtra(URL_EXTRA));
    }

    private void goToAboutActivity() {
        Intent intent = AboutActivity.newIntent(this);
        startActivity(intent);
    }
}
