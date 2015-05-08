package com.calendar.feideng.flunarcalendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by fdeng on 5/4/15.
 * 1. generate each cell view of GridView
 */
public class MonthFragmentAdapter extends BaseAdapter {

    private Context mContext;
    private int mPosition;
    private int mNumofDays = 42;

    public MonthFragmentAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return mNumofDays;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        mPosition = position;
        return mPosition;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.calendar_gridview_cell, null);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.id_gridview_cell_textview);
        String text = mPosition + "adapter";
        textView.setText(text);
        return convertView;
    }
}
