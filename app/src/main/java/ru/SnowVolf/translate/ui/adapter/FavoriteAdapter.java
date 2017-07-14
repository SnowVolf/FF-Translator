package ru.SnowVolf.translate.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.List;

import ru.SnowVolf.translate.R;
import ru.SnowVolf.translate.favorite.FavoriteHolder;
import ru.SnowVolf.translate.favorite.FavoriteItem;
import ru.SnowVolf.translate.ui.fragment.historyfav.FavoriteFragment;
import ru.SnowVolf.translate.util.Preferences;

/**
 * Created by Snow Volf on 04.06.2017, 22:49
 */

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteHolder> {
    private final Context mContext;
    private List<FavoriteItem> mList;

    public FavoriteAdapter(Context context, List<FavoriteItem> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public FavoriteHolder onCreateViewHolder(ViewGroup parent, int type) {
        return new FavoriteHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_favorite, parent, false));
    }

    @Override
    public void onBindViewHolder(FavoriteHolder holder, int position) {
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
    public void onViewDetachedFromWindow(FavoriteHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    public void updateList(List<FavoriteItem> list) {
        mList = list;
        notifyDataSetChanged();
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

        mList.remove(position);
        notifyItemRemoved(position);

        if (mList.isEmpty()) {
            // Show empty status
            FavoriteFragment.refresh();
        }
    }
}

