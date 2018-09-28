package com.example.rus.exercise2_2;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class NewsDetailsActivity extends AppCompatActivity {

    private static final String IMAGE_URL_EXTRA = "imageUrl";
    private static final String TITLE_EXTRA = "title";
    private static final String PUBLISH_DATE_EXTRA = "publishDate";
    private static final String FULL_TEXT_EXTRA = "fullText";
    private static final String CATEGORY_EXTRA = "category";

    public static Intent newIntent(Context context, String imageUrl, String title, String publishDate, String fullText, String category) {
        Intent intent = new Intent(context, NewsDetailsActivity.class);
        intent.putExtra(IMAGE_URL_EXTRA, imageUrl);
        intent.putExtra(TITLE_EXTRA, title);
        intent.putExtra(PUBLISH_DATE_EXTRA, publishDate);
        intent.putExtra(FULL_TEXT_EXTRA, fullText);
        intent.putExtra(CATEGORY_EXTRA, category);
        return intent;
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
        actionBar.setTitle(getIntent().getStringExtra(CATEGORY_EXTRA));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ImageView imageView = findViewById(R.id.detailed_image_view);
        TextView titleTextView = findViewById(R.id.detailed_title_text_view);
        TextView publishDateTextView = findViewById(R.id.detailed_publish_date_text_view);
        TextView fullTextTextView = findViewById(R.id.detailed_full_text_text_view);

        Glide.with(this).load(getIntent().getStringExtra(IMAGE_URL_EXTRA)).into(imageView);
        titleTextView.setText(getIntent().getStringExtra(TITLE_EXTRA));
        publishDateTextView.setText(getIntent().getStringExtra(PUBLISH_DATE_EXTRA));
        fullTextTextView.setText(getIntent().getStringExtra(FULL_TEXT_EXTRA));
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

    private void goToAboutActivity() {
        Intent intent = AboutActivity.newIntent(this);
        startActivity(intent);
    }
}
