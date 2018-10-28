package com.example.rus.exercise2_2;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rus.exercise2_2.network.NYApi;
import com.example.rus.exercise2_2.network.NYEndpoint;
import com.example.rus.exercise2_2.network.dto.NYResponce;
import com.example.rus.exercise2_2.network.dto.Result;
import com.example.rus.exercise2_2.ui.NYAdapter.NYAdapter;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class NewsListActivity extends AppCompatActivity {
    private static final long DELAY = 2;
    private RecyclerView.LayoutManager layoutManager;
    private int spanCount;
    private CompositeDisposable compositeDisposables;
    private LinearLayout errorLinearLayout;
    private TextView errorTextView;
    private Button errorButton;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

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


        setupToolbar();

        loadNews();

/*      //anotherThread
        Single<List<NewsItem>> single = Single.fromCallable(() ->{
            //check if is running in the main thread
            Log.v("TAG", String.valueOf(Thread.currentThread().equals( Looper.getMainLooper().getThread() )));
            System.out.println(Thread.currentThread());
            return DataUtils.generateNews();
        });
        Disposable disposable = single.subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .delay(DELAY, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .subscribe( newsItems -> {
                    progressBar.setVisibility(View.GONE);
                    MyAdapter adapter = new MyAdapter(newsItems, this);
                    //after pr recommendation
                    layoutManager = createLayoutManager();
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);
                    recyclerView.addItemDecoration(new MyItemDecoration(this, R.dimen.item_space, spanCount));
                } );
        compositeDisposables.add(disposable);*/

/*        nyEndpoint.getNewsWithoutKey("home")
                .enqueue(new Callback<NYResponce>() {
                    @Override
                    public void onResponse(Call<NYResponce> call, Response<NYResponce> response) {
                        Log.v("TAG", response.toString());
                        NYResponce nyResponce = response.body();
                        List<Result> resultList = nyResponce.results;
                        NYAdapter adapter = new NYAdapter(resultList, context);
                        layoutManager = createLayoutManager();
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(adapter);
                        recyclerView.addItemDecoration(new MyItemDecoration(context, R.dimen.item_space, spanCount));

                    }

                    @Override
                    public void onFailure(Call<NYResponce> call, Throwable t) {

                    }
                });*/
    }

    private void loadNews() {
        progressBar.setVisibility(View.VISIBLE);

        compositeDisposables = new CompositeDisposable();

        Context context = this;
        NYApi nyApi = NYApi.getInstance();
        NYEndpoint nyEndpoint = nyApi.getNyEndpoint();

        Disposable disposable = nyEndpoint.getNewsRxWithoutKey("home")
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(nyResponce -> {
                            List<Result> resultList = nyResponce.results;
                            progressBar.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            errorLinearLayout.setVisibility(View.GONE);
                            NYAdapter adapter = new NYAdapter(resultList, context);
                            layoutManager = createLayoutManager();
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(adapter);
                            recyclerView.addItemDecoration(new MyItemDecoration(context, R.dimen.item_space, spanCount));
                        },
                        throwable -> {
                            progressBar.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.GONE);
                            errorLinearLayout.setVisibility(View.VISIBLE);
                            errorTextView.setText(throwable.getMessage());
                            errorButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    errorLinearLayout.setVisibility(View.GONE);
                                    loadNews();
                                }
                            });
                        });
        compositeDisposables.add(disposable);
    }

    private void setupToolbar() {
        //toolbar setup
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
