package com.calendar.feideng.flunarcalendar;

import android.content.Context;
import android.content.res.Resources;
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
    private int mMonthIndex;
    private Resources mResources;

    private CalendarCell cell;
    public MonthFragmentAdapter(Context context) {
        mContext = context;
    }

    public MonthFragmentAdapter(Context context, Resources resources, int monthIndex) {
        mContext = context;
        mResources = resources;
        mMonthIndex = monthIndex;
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
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        mPosition = position;

        // using default view
        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.calendar_gridview_cell, null);
        }
        TextView gregorianTextView = (TextView) convertView.findViewById(R.id.id_gridview_cell_gregorian_textview);
        TextView lunarTextView = (TextView) convertView.findViewById(R.id.id_gridview_cell_lunar_textview);
        cell = new CalendarCell(mResources, mMonthIndex, mPosition);
        //Log.i("", mPosition + "- month index: " + mMonthIndex);

        gregorianTextView.setText(cell.getGregorianDateString());
        lunarTextView.setText(cell.getLunarDateString());

        //check weekends
        if (mPosition % 7 == 0 || mPosition % 7 == 6) {
            gregorianTextView.setTextColor(mResources.getColor(R.color.weekends));
        }

        // using custom view
//        if(convertView == null) {
//            convertView = LayoutInflater.from(mContext).inflate(R.layout.sample_calendar_cell_view, null);
//        }
//        CalendarCellView calendarCellView = (CalendarCellView) convertView.findViewById(R.id.id_calendar_cell_view);



        return convertView;
    }

}
