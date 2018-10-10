package com.example.rus.exercise2_2;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyItemDecoration extends RecyclerView.ItemDecoration {

    private int itemOffset, columnNumber;

    public MyItemDecoration(int itemOffset) {
        this.itemOffset = itemOffset;
    }

    public MyItemDecoration(Context context, int itemOffsetId, int columnNumber) {
        this(context.getResources().getDimensionPixelSize(itemOffsetId));
        this.columnNumber = columnNumber;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (parent.getChildAdapterPosition(view) < columnNumber){
            outRect.set(itemOffset, itemOffset, itemOffset, itemOffset);
        } else {
            outRect.set(itemOffset, 0, itemOffset, itemOffset);
        }
    }
}
