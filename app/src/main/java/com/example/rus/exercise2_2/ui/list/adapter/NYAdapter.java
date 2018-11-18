package com.example.rus.exercise2_2.ui.list.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.example.rus.exercise2_2.network.dto.Result;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NYAdapter extends RecyclerView.Adapter<NYViewHolder> {

    private List<Result> resultList;
    private final Context context;

    public NYAdapter(Context context) {
        this.resultList = new ArrayList<>();
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

    public void update(List<Result> resultList) {
        this.resultList = resultList;
        notifyDataSetChanged();
    }
}
