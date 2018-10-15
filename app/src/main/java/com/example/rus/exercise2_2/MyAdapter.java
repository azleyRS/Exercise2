package com.example.rus.exercise2_2;

import android.content.Context;
import android.view.ViewGroup;
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
        switch (newsItems.get(position).getCategory().getName()){
            case "Darwin Awards":
                return R.layout.recycler_view_item_darvin;
            case "Animals":
                return R.layout.recycler_view_item_animal;
            case "Music":
                return R.layout.recycler_view_item_music;
            default:
                return R.layout.recycler_view_item;
        }
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
