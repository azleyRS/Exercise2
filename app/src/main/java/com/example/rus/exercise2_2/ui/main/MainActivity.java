package com.example.rus.exercise2_2.ui.main;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import com.example.rus.exercise2_2.R;
import com.example.rus.exercise2_2.ui.details.NYNewsDetailsFragment;
import com.example.rus.exercise2_2.ui.intro.IntroActivity;
import com.example.rus.exercise2_2.ui.list.NewsListFragment;

public class MainActivity extends AppCompatActivity implements MainActivityFragmentListener {

    private boolean isTwoPanel;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isTwoPanel = findViewById(R.id.frame_detail) != null;
        if (savedInstanceState == null){
            init();
        }
    }

    private void init() {
        NewsListFragment newsListFragment = NewsListFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_list, newsListFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() <= 1) {
            finish();
            return;
        }
    }


    @Override
    public void onNewsItemClick(String url, String category, String title) {
        NYNewsDetailsFragment nyNewsDetailsFragment = NYNewsDetailsFragment.newInstance(url, category, title);
        int frameId = isTwoPanel? R.id.frame_detail : R.id.frame_list;
        if (getSupportFragmentManager().findFragmentByTag(title)==null)
        getSupportFragmentManager()
                .beginTransaction()
                .replace(frameId, nyNewsDetailsFragment, title)
                .addToBackStack(title)
                .commit();
    }
}
