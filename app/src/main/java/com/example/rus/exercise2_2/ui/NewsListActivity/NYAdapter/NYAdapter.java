package com.example.rus.exercise2_2.ui.NewsListActivity.NYAdapter;

import android.content.Context;
import android.view.ViewGroup;

import com.example.rus.exercise2_2.network.dto.Result;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NYAdapter extends RecyclerView.Adapter<NYViewHolder> {

    private final List<Result> resultList;
    private final Context context;

    public NYAdapter(List<Result> resultList, Context context) {
        this.resultList = resultList;
        this.context = context;
    }

    @NonNull
    @Override
    public NYViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return NYViewHolder.create(parent, context);

    }

    @Override
    public void onBindViewHolder(@NonNull NYViewHolder holder, int position) {
        holder.bind(resultList.get(position));
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }
}
