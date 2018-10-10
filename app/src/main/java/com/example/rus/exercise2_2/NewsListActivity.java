package com.example.rus.exercise2_2;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class NewsListActivity extends AppCompatActivity {
    private RecyclerView.LayoutManager layoutManager;
    private int spanCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);
        init();
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


    private void init() {
        //toolbar setup
        Toolbar toolbar = findViewById(R.id.news_list_activity_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.ny_times);
        }
        //recyclerview setup
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        MyAdapter adapter = new MyAdapter(DataUtils.generateNews(), this);
        //after pr recommendation
        layoutManager = createLayoutManager();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new MyItemDecoration(this, R.dimen.item_space, spanCount));
    }

    //after pr recommendation
    private RecyclerView.LayoutManager createLayoutManager() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            spanCount = 1;
            return new LinearLayoutManager(this);
        } else {
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
            spanCount = (int) (dpWidth / 300);
            return new GridLayoutManager(this, spanCount);
        }
    }


    private void goToAboutActivity() {
        Intent intent = AboutActivity.newIntent(this);
        startActivity(intent);
    }
}
