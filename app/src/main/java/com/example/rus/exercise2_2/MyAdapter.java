package com.example.rus.exercise2_2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private final List<NewsItem> newsItems;
    private Context context;

    public MyAdapter(List<NewsItem> newsItems, Context context) {
        this.newsItems = newsItems;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // i made it that way because MyViewHolder is inner class, later will try to make it external with static create method
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //best practice?
        holder.bind(newsItems.get(position));
        //before best practice
/*        holder.categoryTextView.setText(newsItems.get(position).getCategory().getName());
        holder.previewTextView.setText(newsItems.get(position).getPreviewText());
        holder.publishDateTextView.setText(TimesUtil.getTimeAgo(newsItems.get(position).getPublishDate()));
        holder.titleTextView.setText(newsItems.get(position).getTitle());
        Glide.with(context).load(newsItems.get(position).getImageUrl()).into(holder.imageView);*/
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return newsItems.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView categoryTextView, previewTextView, publishDateTextView, titleTextView;
        ImageView imageView;
        private int position;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryTextView = itemView.findViewById(R.id.category_text_view);
            previewTextView = itemView.findViewById(R.id.preview_text_view);
            publishDateTextView = itemView.findViewById(R.id.publish_date_text_view);
            titleTextView = itemView.findViewById(R.id.title_text_view);
            imageView = itemView.findViewById(R.id.image_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToNewsDetailsActivity(position);
                }
            });
        }

        //wanted to pass this position in bundle, but i can probably use 5 extras to pass information
        //or make NewsItem Parcelable
        //or i can save newsList somewhere (SQLite?)
        //method below remains until i'll find best solution
        public void bind(int position) {
            this.position = position;
        }

        //best practice?
        public void bind(NewsItem newsItem) {
            categoryTextView.setText(newsItem.getCategory().getName());
            previewTextView.setText(newsItem.getPreviewText());
            publishDateTextView.setText(TimesUtil.getTimeAgo(newsItem.getPublishDate()));
            titleTextView.setText(newsItem.getTitle());
            Glide.with(context).load(newsItem.getImageUrl()).into(imageView);
        }
    }

    private void goToNewsDetailsActivity(int position) {
        String imageUrl = newsItems.get(position).getImageUrl();
        String title = newsItems.get(position).getTitle();
        String publishDate = TimesUtil.getTimeAgo(newsItems.get(position).getPublishDate());
        String fullText = newsItems.get(position).getFullText();
        String category = newsItems.get(position).getCategory().getName();
        Intent intent = NewsDetailsActivity.newIntent(context, imageUrl, title, publishDate, fullText, category);
        context.startActivity(intent);
    }
}
