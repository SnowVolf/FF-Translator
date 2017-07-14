package ru.SnowVolf.translate.ui.widget.recyclerview;

import android.graphics.Canvas;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import ru.SnowVolf.translate.util.interfaces.ITouchHelper;

/**
 * Created by Snow Volf on 25.06.2017, 1:54
 */

public class RecyclerViewDragCallBack extends ItemTouchHelper.Callback {
    public static final float ALPHA_FULL = 1.0f;
    private final ITouchHelper mAdapter;

    public RecyclerViewDragCallBack(ITouchHelper adapter){
        mAdapter = adapter;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
       if (recyclerView.getLayoutManager() instanceof GridLayoutManager){
           final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
           final int swipeFlags = 0;
           return makeMovementFlags(dragFlags, swipeFlags);
       } else {
           final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
           final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
           return makeMovementFlags(dragFlags, swipeFlags);
       }
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        if (viewHolder.getItemViewType() != target.getItemViewType()){
            return false;
        }
        mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mAdapter.onItemDimiss(viewHolder.getAdapterPosition());
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
            final float alpha = ALPHA_FULL - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
            viewHolder.itemView.setAlpha(alpha);
            viewHolder.itemView.setTranslationX(dX);
        } else {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }
}
