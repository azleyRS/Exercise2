package com.example.rus.exercise2_2.ui.NewsDetailsActivity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rus.exercise2_2.db.AppDatabase;
import com.example.rus.exercise2_2.db.NewsEntity;
import com.example.rus.exercise2_2.ui.AboutActivity.AboutActivity;
import com.example.rus.exercise2_2.R;
import com.example.rus.exercise2_2.ui.NewsListActivity.NewsListActivity;

import java.util.concurrent.Callable;

public class NYNewsDetailsActivity extends AppCompatActivity {

    private static final String URL_EXTRA = "url";
    private static final String CATEGORY_EXTRA = "category";
    private static final String TITLE_EXTRA = "title";
    private CompositeDisposable compositeDisposables;

    ImageView imageView;
    TextView titleTextView, publishDateTextView, fullTextTextView;
    Button button;
    private NewsEntity newsEntity;


    public static Intent newIntent(Context context, String url, String category, String title) {
        Intent intent = new Intent(context, NYNewsDetailsActivity.class);
        intent.putExtra(URL_EXTRA, url);
        if (!category.isEmpty()){
            intent.putExtra(CATEGORY_EXTRA, category);
        }
        intent.putExtra(TITLE_EXTRA, title);
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
    protected void onPause() {
        super.onPause();
        compositeDisposables.clear();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        init();
    }

    private void init() {
        Toolbar toolbar = findViewById(R.id.news_details_activity_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        //after pr recommendation
        if (actionBar != null) {
            if (getIntent().hasExtra(CATEGORY_EXTRA)){
                actionBar.setTitle(getIntent().getStringExtra(CATEGORY_EXTRA));
            }
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        compositeDisposables = new CompositeDisposable();


        //WebView webView = findViewById(R.id.webview);
        //webView.loadUrl(getIntent().getStringExtra(URL_EXTRA));

        imageView = findViewById(R.id.detailed_image_view);
        fullTextTextView = findViewById(R.id.detailed_full_text_text_view);
        publishDateTextView = findViewById(R.id.detailed_publish_date_text_view);
        titleTextView = findViewById(R.id.detailed_title_text_view);
        button = findViewById(R.id.delete_button);
        button.setOnClickListener(v -> delete());


        loadFromDb();
    }

    private void delete() {
        Disposable disposable = Completable.fromCallable((Callable<Void>) () -> {
            AppDatabase.getAppDatabase(getApplicationContext()).newsDao().delete(newsEntity);
            return null;
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::toNewsListActivity);
        compositeDisposables.add(disposable);
    }

    private void toNewsListActivity() {
        Intent intent = NewsListActivity.newIntent(this);
        startActivity(intent);
        finish();
    }

    private void loadFromDb() {
        String id = getIntent().getStringExtra(TITLE_EXTRA) + getIntent().getStringExtra(URL_EXTRA);
        Disposable disposable = AppDatabase.getAppDatabase(getApplicationContext()).newsDao().getNewsById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(newsEntities -> {
                    newsEntity = newsEntities;
                    Glide.with(this).load(newsEntities.multimediaUrl).into(imageView);
                    fullTextTextView.setText(newsEntities._abstract);
                    titleTextView.setText(newsEntities.title);
                    publishDateTextView.setText(newsEntities.publishedDate);
                });
        compositeDisposables.add(disposable);
    }

    private void goToAboutActivity() {
        Intent intent = AboutActivity.newIntent(this);
        startActivity(intent);
    }
}
