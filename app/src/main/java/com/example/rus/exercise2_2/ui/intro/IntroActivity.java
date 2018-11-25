package com.example.rus.exercise2_2.ui.intro;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.Completable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.example.rus.exercise2_2.R;
import com.example.rus.exercise2_2.ui.list.NewsListFragment;

import java.util.concurrent.TimeUnit;

public class IntroActivity extends AppCompatActivity {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final static String NEED_TO_SHOW = "needToShow";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (needToShowIntro()) {
            setContentView(R.layout.activity_intro);
            Disposable disposable = Completable.complete()
                    .delay(3, TimeUnit.SECONDS)
                    .subscribe(this::startSecondActivity);
            compositeDisposable.add(disposable);
        } else {
            startSecondActivity();
        }
    }

    private boolean needToShowIntro() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (!preferences.contains(NEED_TO_SHOW)){
            preferences.edit().putBoolean(NEED_TO_SHOW, true).apply();
        }
        boolean isNeedToShowIntro = preferences.getBoolean(NEED_TO_SHOW, true);
        preferences.edit().putBoolean(NEED_TO_SHOW, !isNeedToShowIntro).apply();
        return isNeedToShowIntro;
    }

    private void startSecondActivity() {
        Intent intent = NewsListFragment.newIntent(this);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        compositeDisposable.dispose();
    }

}
