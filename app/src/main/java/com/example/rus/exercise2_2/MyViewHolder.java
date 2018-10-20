package com.example.rus.exercise2_2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {
    private TextView categoryTextView, previewTextView, publishDateTextView, titleTextView;
    private ImageView imageView;
    private NewsItem newsItem;
    private Context context;

    public static MyViewHolder create(ViewGroup parent, Context context, int layoutRes){
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutRes,parent,false);
        return new MyViewHolder(view, context);
    }

    //best practice?
    public void bind(NewsItem newsItem) {
        this.newsItem = newsItem;
        categoryTextView.setText(newsItem.getCategory().getName());
        previewTextView.setText(newsItem.getPreviewText());
        publishDateTextView.setText(TimesUtil.getTimeAgo(newsItem.getPublishDate()));
        titleTextView.setText(newsItem.getTitle());
        Glide.with(context).load(newsItem.getImageUrl()).into(imageView);
    }

    private MyViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.context = context;
        categoryTextView = itemView.findViewById(R.id.category_text_view);
        previewTextView = itemView.findViewById(R.id.preview_text_view);
        publishDateTextView = itemView.findViewById(R.id.publish_date_text_view);
        titleTextView = itemView.findViewById(R.id.title_text_view);
        imageView = itemView.findViewById(R.id.image_view);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToNewsDetailsActivity();
            }
        });
    }

    private void goToNewsDetailsActivity() {
        //after pr recommendation
        Intent intent = NewsDetailsActivity.newIntent(context, newsItem);
        context.startActivity(intent);
    }


}
