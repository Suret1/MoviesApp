package com.suret.moviesapp.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class EqualSpacingItemDecoration(
    private val spacing: Int,
    private var orientation: Int = -1,
    private var includeEdge: Boolean
) : ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildViewHolder(view).bindingAdapterPosition
        val itemCount = state.itemCount
        setSpacingForDirection(outRect, parent, position, itemCount, view)
    }

    private fun setSpacingForDirection(
        outRect: Rect,
        parent: RecyclerView,
        position: Int,
        itemCount: Int,
        view: View
    ) {
        if (orientation == -1) {
            orientation = resolveOrientation(parent.layoutManager)
        }
        when (orientation) {
            HORIZONTAL -> {
                outRect.left = spacing
                outRect.right = if (position == itemCount - 1) spacing else 0
                outRect.top = spacing
                outRect.bottom = spacing
            }
            VERTICAL -> {
                outRect.left = spacing
                outRect.right = spacing
                outRect.top = spacing
                outRect.bottom = if (position == itemCount - 1) spacing else 0
            }
            GRID -> {
                if (parent.layoutManager is GridLayoutManager) {
                    val layoutManager = parent.layoutManager as GridLayoutManager?
                    val spanCount = layoutManager!!.spanCount
                    val itemPos = parent.getChildAdapterPosition(view) // item position
                    val column = itemPos % spanCount // item column
                    if (includeEdge) {
                        outRect.left =
                            spacing - column * spacing / spanCount // spacing - column * ((1f / spanCount) * spacing)
                        outRect.right =
                            (column + 1) * spacing / spanCount // (column + 1) * ((1f / spanCount) * spacing)
                        if (itemPos < spanCount) { // top edge
                            outRect.top = spacing
                        }
                        outRect.bottom = spacing // item bottom
                    } else {
                        outRect.left =
                            column * spacing / spanCount // column * ((1f / spanCount) * spacing)
                        outRect.right =
                            spacing - (column + 1) * spacing / spanCount // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                        if (itemPos >= spanCount) {
                            outRect.top = spacing // item top
                        }
                    }
                }
            }
        }
    }

    private fun resolveOrientation(layoutManager: RecyclerView.LayoutManager?): Int {
        if (layoutManager is GridLayoutManager) return GRID
        return if (layoutManager!!.canScrollHorizontally()) HORIZONTAL else VERTICAL
    }

    companion object {
        const val HORIZONTAL = 0
        const val VERTICAL = 1
        const val GRID = 2
    }
}