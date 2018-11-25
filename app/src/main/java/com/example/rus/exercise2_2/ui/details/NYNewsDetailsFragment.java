package com.example.rus.exercise2_2.ui.details;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rus.exercise2_2.db.AppDatabase;
import com.example.rus.exercise2_2.db.NewsEntity;
import com.example.rus.exercise2_2.ui.about.AboutActivity;
import com.example.rus.exercise2_2.R;
import com.example.rus.exercise2_2.ui.list.NewsListFragment;

import java.util.concurrent.Callable;

public class NYNewsDetailsFragment extends Fragment {

    private static final String URL_EXTRA = "url";
    private static final String CATEGORY_EXTRA = "category";
    private static final String TITLE_EXTRA = "title";
    private CompositeDisposable compositeDisposables;

    ImageView imageView;
    TextView titleTextView, publishDateTextView, fullTextTextView;
    Button button;
    private NewsEntity newsEntity;

    public static NYNewsDetailsFragment newInstance(String url, String category, String title){
        NYNewsDetailsFragment fragment = new NYNewsDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(URL_EXTRA, url);
        if (!category.isEmpty()){
            bundle.putString(CATEGORY_EXTRA, category);
        }
        bundle.putString(TITLE_EXTRA, title);
        fragment.setArguments(bundle);
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
    public void onPause() {
        super.onPause();
        compositeDisposables.clear();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_details, container, false);
        init(view);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.news_list_activity_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void init(View view) {
        Toolbar toolbar = view.findViewById(R.id.news_details_fragment_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        //after pr recommendation
        if (actionBar != null) {
            if (getArguments().getString(CATEGORY_EXTRA)!=null){
                actionBar.setTitle(getArguments().getString(CATEGORY_EXTRA));
            }
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationOnClickListener(v -> toNewsListActivity());

        compositeDisposables = new CompositeDisposable();

        imageView = view.findViewById(R.id.detailed_image_view);
        fullTextTextView = view.findViewById(R.id.detailed_full_text_text_view);
        publishDateTextView = view.findViewById(R.id.detailed_publish_date_text_view);
        titleTextView = view.findViewById(R.id.detailed_title_text_view);
        button = view.findViewById(R.id.delete_button);
        button.setOnClickListener(v -> delete());


        loadFromDb();
    }

    private void delete() {
        Disposable disposable = Completable.fromCallable((Callable<Void>) () -> {
            AppDatabase.getAppDatabase(getActivity().getApplicationContext()).newsDao().delete(newsEntity);
            return null;
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::toNewsListActivity);
        compositeDisposables.add(disposable);
    }

    private void toNewsListActivity() {
        /*Intent intent = NewsListFragment.newIntent(getActivity());
        startActivity(intent);
        getActivity().finish();*/
        getActivity().getSupportFragmentManager().popBackStack();
    }

    private void loadFromDb() {
        String id = getArguments().getString(TITLE_EXTRA) + getArguments().getString(URL_EXTRA);
        Disposable disposable = AppDatabase.getAppDatabase(getActivity().getApplicationContext()).newsDao().getNewsById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(newsEntities -> {
                    newsEntity = newsEntities;
                    Glide.with(this).load(newsEntities.multimediaUrl).into(imageView);
                    fullTextTextView.setText(newsEntities.previewText);
                    titleTextView.setText(newsEntities.title);
                    publishDateTextView.setText(newsEntities.publishedDate);
                });
        compositeDisposables.add(disposable);
    }

    private void goToAboutActivity() {
        Intent intent = AboutActivity.newIntent(getActivity());
        startActivity(intent);
    }
}
