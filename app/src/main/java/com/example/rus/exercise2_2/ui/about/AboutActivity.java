package com.example.rus.exercise2_2.ui.about;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rus.exercise2_2.R;

import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {
    private final String fbUrl = "https://www.facebook.com/";
    private final String vkUrl = "https://vk.com/";
    private final String instagramUrl = "https://www.instagram.com/";
    private final String email = "azley@mail.ru";
    private final String subject = "Hello, Android Academy MSK!";
    private EditText messageEditText;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, AboutActivity.class);
        return intent;
    }

    //develop branch
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        if (getSupportActionBar()!=null){
            getSupportActionBar().setTitle(R.string.name);
        }
        ImageView fbImageView = findViewById(R.id.fb_image_view);
        ImageView vkImageView = findViewById(R.id.vk_image_view);
        ImageView instagramImageView = findViewById(R.id.instagram_image_view);
        messageEditText = findViewById(R.id.edit_text);
        Button sendButton = findViewById(R.id.send_button);


        fbImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl(fbUrl);
            }
        });

        vkImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl(vkUrl);
            }
        });

        instagramImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl(instagramUrl);
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openExercise1Activity();
            }
        });

        addDisclaimer();
    }

    private void openExercise1Activity() {
        String message = messageEditText.getText().toString();
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);
        if (intent.resolveActivity(getPackageManager())!= null){
            startActivity(intent);
        } else {
            Toast.makeText(this, R.string.no_email_app, Toast.LENGTH_SHORT).show();
        }
    }

    private void addDisclaimer() {
        LinearLayout linearLayout = findViewById(R.id.main_linear_layout);
        TextView textView = new TextView(this);
        textView.setText(R.string.disclaimer);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        linearLayout.addView(textView);
    }

    private void openUrl(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        if (i.resolveActivity(getPackageManager()) != null){
            startActivity(i);
        } else {
            Toast.makeText(this, R.string.no_browser_found, Toast.LENGTH_SHORT).show();
        }
    }
}