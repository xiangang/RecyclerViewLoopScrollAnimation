package com.nxg.app.main

import androidx.recyclerview.widget.RecyclerView

import android.view.View

import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView.ItemDecoration


class DoubleColorBallItemDecoration(private val space: Int) : ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView, state: RecyclerView.State
    ) {
        if (parent.getChildAdapterPosition(view) != 0) outRect.left = space
    }
}