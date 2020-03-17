package com.example.huarongdao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.huarongdao.R;
import com.example.huarongdao.bean.RecordEntity;
import com.example.huarongdao.util.SharedPreferencesUtil;
import com.example.huarongdao.util.TimingUtil;

import java.util.ArrayList;
import java.util.List;

public class RecordAdapter extends BaseAdapter {

    private List<RecordEntity> data;
    private LayoutInflater inflater;
    /**
     * 最佳时间
     */
    private int optimal;

    public RecordAdapter(List<RecordEntity> data, Context context, int difficulty) {
        this.data = data;
        inflater = LayoutInflater.from(context);
        optimal = SharedPreferencesUtil.getSharedPreference(context).getInt(String.valueOf(difficulty), 0);
    }

    public void refresh(List<RecordEntity> list) {
        if (list == null) {
            list = new ArrayList<>();
        }
        data.clear();
        data.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public RecordEntity getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_record, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        RecordEntity item = getItem(position);
        if (item.getTiming() <= optimal) {
            holder.iv.setVisibility(View.VISIBLE);
        } else {
            holder.iv.setVisibility(View.GONE);
        }

        holder.tvTiming.setText(TimingUtil.timing2String(item.getTiming()));
        holder.tvDatetime.setText(item.getDatetime());
        return convertView;
    }

    private static class ViewHolder {

        View itemView;
        ImageView iv;
        TextView tvTiming;
        TextView tvDatetime;

        public ViewHolder(View itemView) {
            this.itemView = itemView;
            iv = itemView.findViewById(R.id.iv);
            tvTiming = itemView.findViewById(R.id.tv_timing);
            tvDatetime = itemView.findViewById(R.id.tv_datetime);
        }
    }

}
