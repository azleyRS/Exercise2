package com.example.rus.exercise2_2.ui.list;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.rus.exercise2_2.ui.main.MainActivityFragmentListener;
import com.example.rus.exercise2_2.utils.FromNetToDbConverter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.concurrent.Callable;

public class NewsListFragment extends Fragment {
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

    private NYAdapter adapter;

    public static NewsListFragment newInstance(){
        NewsListFragment fragment = new NewsListFragment();
        return fragment;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_list, container, false);
        init(view);
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        compositeDisposables.clear();
    }

    private void init(View view) {
        errorLinearLayout = view.findViewById(R.id.error_linear_layout);
        errorTextView = view.findViewById(R.id.error_text_view);
        errorButton = view.findViewById(R.id.error_button);
        recyclerView = view.findViewById(R.id.recycler_view);
        progressBar = view.findViewById(R.id.news_list_activity_progress_bar);
        sectionTextView = view.findViewById(R.id.section_text_view);
        loadFAB = view.findViewById(R.id.floatingActionButton);

        setupToolbar(view);
        setupSectionTextView();

        compositeDisposables = new CompositeDisposable();

        adapter = new NYAdapter( getActivity());
        recyclerView.setAdapter(adapter);
        layoutManager = createLayoutManager();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new MyItemDecoration(getActivity(), R.dimen.item_space, spanCount));
        setupFAB();
        load();
    }

    private void setupFAB() {
        loadFAB.setOnClickListener(v -> loadNews(checkedItem));
    }

    private void setupSectionTextView() {
        Context context = getActivity();
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


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.news_list_activity_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.news_list_activity_menu,menu);
        return true;
    }*/

    private void setupToolbar(View view) {
        Toolbar toolbar = view.findViewById(R.id.news_list_activity_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.ny_times);
        }
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

        //save to db
        Disposable disposable = Completable.fromCallable((Callable<Void>) () -> {
            AppDatabase db = AppDatabase.getAppDatabase(getActivity().getApplicationContext());
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
        Disposable disposable = AppDatabase.getAppDatabase(getActivity().getApplicationContext()).newsDao().getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(newsEntities -> {
                    adapter.updateWithNewsEntities(newsEntities);
                    if (!newsEntities.isEmpty())
                    sectionTextView.setVisibility(View.VISIBLE);
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

    //after pr recommendation
    private RecyclerView.LayoutManager createLayoutManager() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            spanCount = 1;
            return new LinearLayoutManager(getActivity());
        } else {
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
            spanCount = (int) (dpWidth / 300);
            return new GridLayoutManager(getActivity(), spanCount);
        }
    }


    private void goToAboutActivity() {
        Intent intent = AboutActivity.newIntent(getActivity());
        startActivity(intent);
    }
}
