package ru.SnowVolf.translate.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.List;

import ru.SnowVolf.translate.R;
import ru.SnowVolf.translate.history.HistoryHolder;
import ru.SnowVolf.translate.history.HistoryItem;
import ru.SnowVolf.translate.util.Preferences;

/**
 * Created by Snow Volf on 30.05.2017, 21:14
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryHolder> {

    private final Context mContext;
    private List<HistoryItem> mList;

    public HistoryAdapter(Context context, List<HistoryItem> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public HistoryHolder onCreateViewHolder(ViewGroup parent, int type) {
        return new HistoryHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent, false));
    }

    @Override
    public void onBindViewHolder(HistoryHolder holder, int position) {
        Animation anim = AnimationUtils.loadAnimation(mContext,
                (position > -1) ? R.anim.rv_bottom_anim : R.anim.rv_top_anim);
        holder.setData(mContext, mList.get(position));
        if (Preferences.isListAnimAllowed())
        holder.itemView.startAnimation(anim);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    //Clear RV animation
    @Override
    public void onViewDetachedFromWindow(HistoryHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    public void updateList(List<HistoryItem> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public void removeItemAtPosition(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
//
//        if (mList.isEmpty()) {
//            // Show empty status
//            ((HistoryActivity) mContext).refresh();
//        }
    }

    public void removeItem(long id) {
        int position = 0;
        for (; position < mList.size(); position++) {
            if (mList.get(position).getId() == id) {
                break;
            }
        }

        if (position == mList.size()) {
            return;
        }
        removeItemAtPosition(position);
    }
}

