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

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private final List<NewsItem> newsItems;
    private Context context;

    public MyAdapter(List<NewsItem> newsItems, Context context) {
        this.newsItems = newsItems;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        int result;
        switch (newsItems.get(position).getCategory().getName()){
            case "Darwin Awards":
                result = R.layout.recycler_view_item_darvin;
                break;
            case "Animals":
                result = R.layout.recycler_view_item_animal;
                break;
            case "Music":
                result = R.layout.recycler_view_item_music;
                break;
            default:
                result = R.layout.recycler_view_item;
                break;
        }
        return result;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return MyViewHolder.create(parent, context, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //best practice?
        holder.bind(newsItems.get(position));
    }

    @Override
    public int getItemCount() {
        return newsItems.size();
    }
}
