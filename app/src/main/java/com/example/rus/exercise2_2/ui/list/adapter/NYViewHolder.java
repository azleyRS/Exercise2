package com.example.rus.exercise2_2.ui.list.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.rus.exercise2_2.R;
import com.example.rus.exercise2_2.db.NewsEntity;
import com.example.rus.exercise2_2.network.dto.Result;
import com.example.rus.exercise2_2.ui.details.NYNewsDetailsActivity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NYViewHolder extends RecyclerView.ViewHolder {
    private TextView categoryTextView, previewTextView, publishDateTextView, titleTextView;
    private ImageView imageView;
    private NewsEntity resultItem;
    private Context context;

    public static NYViewHolder create(ViewGroup parent, Context context){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item,parent,false);
        return new NYViewHolder(view, context);
    }

    public void bind(NewsEntity resultItem) {
        this.resultItem = resultItem;
        if (resultItem.subsection.isEmpty()){
            categoryTextView.setVisibility(View.GONE);
        } else {
            categoryTextView.setVisibility(View.VISIBLE);
            categoryTextView.setText(resultItem.subsection);
        }
        previewTextView.setText(resultItem.previewText);
        publishDateTextView.setText(resultItem.publishedDate);
        titleTextView.setText(resultItem.title);
        //later
        if (resultItem.multimediaUrl != null){
            Glide.with(context).load(resultItem.multimediaUrl)
                    .apply(new RequestOptions().error(R.drawable.ic_no_image).placeholder(R.drawable.ic_no_image).fitCenter())
                    .into(imageView);
        } else {
            imageView.setImageResource(R.drawable.ic_no_image);
        }
    }

    public NYViewHolder(@NonNull View itemView, Context context) {
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
        //later
        Intent intent = NYNewsDetailsActivity.newIntent(context, resultItem.url, resultItem.subsection, resultItem.title);
        context.startActivity(intent);
    }
}
