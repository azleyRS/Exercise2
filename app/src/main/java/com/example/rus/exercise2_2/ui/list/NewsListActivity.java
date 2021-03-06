package com.example.rus.exercise2_2.ui.list;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.rus.exercise2_2.db.AppDatabase;
import com.example.rus.exercise2_2.db.NewsEntity;
import com.example.rus.exercise2_2.ui.about.AboutActivity;
import com.example.rus.exercise2_2.R;
import com.example.rus.exercise2_2.network.NYApi;
import com.example.rus.exercise2_2.network.NYEndpoint;
import com.example.rus.exercise2_2.network.dto.NYResponce;
import com.example.rus.exercise2_2.network.dto.Result;
import com.example.rus.exercise2_2.ui.list.adapter.NYAdapter;
import com.example.rus.exercise2_2.utils.FromNetToDbConverter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.concurrent.Callable;

public class NewsListActivity extends AppCompatActivity {
    private RecyclerView.LayoutManager layoutManager;
    private int spanCount;
    private CompositeDisposable compositeDisposables;
    private LinearLayout errorLinearLayout;
    private TextView errorTextView, sectionTextView;
    private Button errorButton;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private FloatingActionButton loadFAB;
    private int checkedItem = 0;

    NYAdapter adapter;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, NewsListActivity.class);
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
        setContentView(R.layout.activity_news_list);
        init();
    }

    @Override
    protected void onPause() {
        super.onPause();
        compositeDisposables.clear();
    }

    private void init() {
        errorLinearLayout = findViewById(R.id.error_linear_layout);
        errorTextView = findViewById(R.id.error_text_view);
        errorButton = findViewById(R.id.error_button);
        recyclerView = findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.news_list_activity_progress_bar);
        sectionTextView = findViewById(R.id.section_text_view);
        loadFAB = findViewById(R.id.floatingActionButton);

        setupSectionTextView();

        setupToolbar();

        compositeDisposables = new CompositeDisposable();

        adapter = new NYAdapter( this);
        recyclerView.setAdapter(adapter);
        layoutManager = createLayoutManager();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new MyItemDecoration(this, R.dimen.item_space, spanCount));
        setupFAB();

        //this is for testing
        load();
        //loadNews(checkedItem);
    }

    private void setupFAB() {
        loadFAB.setOnClickListener(v -> loadNews(checkedItem));
    }

    private void setupSectionTextView() {
        Context context = this;
        sectionTextView.setText(getResources().getStringArray(R.array.news_sections)[checkedItem]);
        sectionTextView.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setSingleChoiceItems(R.array.news_sections, checkedItem, (dialog, which) -> {
                String section = getResources().getStringArray(R.array.news_sections)[which];
                sectionTextView.setText(section);
                checkedItem = which;
                loadNews(checkedItem);
                dialog.dismiss();
            });
            builder.show();
        });
    }

    private void loadNews(int checkedItem) {
        progressBar.setVisibility(View.VISIBLE);
        NYEndpoint nyEndpoint = NYApi.getInstance().getNyEndpoint();

        Disposable disposable = nyEndpoint.getNewsRxWithoutKey(getResources().getStringArray(R.array.news_sections)[checkedItem].toLowerCase())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponse, this::handleErrors);
        compositeDisposables.add(disposable);
    }

    private void handleResponse(NYResponce nyResponce){
        List<Result> resultList = nyResponce.results;
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        sectionTextView.setVisibility(View.VISIBLE);
        errorLinearLayout.setVisibility(View.GONE);
        //adapter.update(resultList);

        //save to db
        Disposable disposable = Completable.fromCallable((Callable<Void>) () -> {
            AppDatabase db = AppDatabase.getAppDatabase(getApplicationContext());
            db.newsDao().deleteAll();
            for (Result result: resultList){
                NewsEntity entity = FromNetToDbConverter.toDatabase(result);
                db.newsDao().insert(entity);
            }
            return null;
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::load);
        compositeDisposables.add(disposable);
    }

    private void load() {
        Disposable disposable = AppDatabase.getAppDatabase(getApplicationContext()).newsDao().getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(newsEntities -> {
                    /*List<Result> resultList = new ArrayList<>();
                    for (NewsEntity entity: newsEntities){
                        resultList.add(FromNetToDbConverter.fromDatabase(entity));
                    }
                    adapter.update(resultList);*/
                    adapter.updateWithNewsEntities(newsEntities);
                });
        compositeDisposables.add(disposable);
    }

    private void handleErrors(Throwable throwable){
        progressBar.setVisibility(View.GONE);
        sectionTextView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        errorLinearLayout.setVisibility(View.VISIBLE);
        errorTextView.setText(throwable.getMessage());
        errorButton.setOnClickListener(v -> {
            errorLinearLayout.setVisibility(View.GONE);
            loadNews(checkedItem);
        });
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.news_list_activity_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.ny_times);
        }
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
