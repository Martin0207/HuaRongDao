package com.example.huarongdao.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.huarongdao.R;
import com.example.huarongdao.util.SharedPreferencesUtil;
import com.example.huarongdao.util.TimingUtil;

import java.util.List;

public class DifficultyAdapter extends BaseAdapter {

    /**
     * 数据源
     */
    private List<Integer> data;

    /**
     * 布局加载器
     */
    private LayoutInflater inflater;

    /**
     * 偏好设置
     */
    private SharedPreferences sharedPreferences;

    public DifficultyAdapter(Context context, List<Integer> data) {
        this.data = data;
        this.inflater = LayoutInflater.from(context);
        sharedPreferences = SharedPreferencesUtil.getSharedPreference(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Integer getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_difficulty, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Integer item = getItem(position);
        holder.tvDifficulty.setText(String.format("%d X %d", item, item));
        int record = sharedPreferences.getInt(item.toString(), 0);
        if (record <= 0) {
            holder.tvRecord.setText("");
        } else {
            holder.tvRecord.setText(TimingUtil.timing2String(record));
        }
        return convertView;
    }

    private static class ViewHolder {

        View contentView;

        TextView tvDifficulty;

        TextView tvRecord;

        public ViewHolder(View contentView) {
            this.contentView = contentView;
            tvDifficulty = contentView.findViewById(R.id.tv_difficulty);
            tvRecord = contentView.findViewById(R.id.tv_record);
        }
    }

}
