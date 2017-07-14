package ru.SnowVolf.translate.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import ru.SnowVolf.translate.R;
import ru.SnowVolf.translate.ui.widget.recyclerview.SimpleHolder;
import ru.SnowVolf.translate.ui.widget.recyclerview.SimpleItem;

/**
 * Created by Snow Volf on 15.06.2017, 4:51
 */

public class SimpleRVAdapter extends RecyclerView.Adapter<SimpleHolder>  {

    private final Context mContext;
    private List<SimpleItem> mList;

    public SimpleRVAdapter(Context context, List<SimpleItem> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public SimpleHolder onCreateViewHolder(ViewGroup parent, int type) {
        return new SimpleHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_simple_recyclerview, parent, false));
    }

    @Override
    public void onBindViewHolder(SimpleHolder holder, int position) {
        holder.setData(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void updateList(List<SimpleItem> list) {
        mList = list;
        notifyDataSetChanged();
    }

}
