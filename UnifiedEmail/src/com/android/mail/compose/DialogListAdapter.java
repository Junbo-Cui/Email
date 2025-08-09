package com.android.mail.compose;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.mail.R;
import java.util.ArrayList;
import android.util.Log;

public class DialogListAdapter extends BaseAdapter {
    private ArrayList mData;
    private Context context;

    public DialogListAdapter(Context context, ArrayList data) {
        this.context = context;
        this.mData = data;
    }

    @Override
    public int getCount() {
        return null != mData ? mData.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_layout, null);
            holder = new ViewHolder();
            holder.mTV = (TextView) convertView.findViewById(R.id.tv_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mTV.setText((String) mData.get(position));
        return convertView;
    }

    static class ViewHolder {
        TextView mTV;
    }
}
