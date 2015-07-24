package com.calendar.feideng.flunarcalendar;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.calendar.feideng.module.DateFormatter;
import com.calendar.feideng.module.LunarCalendar;

import java.util.Calendar;
import java.util.GregorianCalendar;

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
    private long firstDayMillis = 0;
    private int solarTerm1 = 0;
    private int solarTerm2 = 0;
    private DateFormatter fomatter;

    private String mGregorianDateString = "";
    private String mLunarDateString = "";

   // private CalendarCell cell;
    public MonthFragmentAdapter(Context context) {
        mContext = context;
    }

    public MonthFragmentAdapter(Context context, Resources resources, int monthIndex) {
        mContext = context;
        mResources = resources;
        mMonthIndex = monthIndex;
        int year = LunarCalendar.getMinYear() + (mMonthIndex / 12);
        int month = mMonthIndex % 12;
        Calendar date = new GregorianCalendar(year, month, 1);
        int offset = 1 - date.get(Calendar.DAY_OF_WEEK);
        date.add(Calendar.DAY_OF_MONTH, offset);
        firstDayMillis = date.getTimeInMillis();
        solarTerm1 = LunarCalendar.getSolarTerm(year, month * 2 + 1);
        solarTerm2 = LunarCalendar.getSolarTerm(year, month * 2 + 2);
        fomatter = new DateFormatter(resources);
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

        //start to calculate date
        LunarCalendar date = new LunarCalendar(firstDayMillis + position * LunarCalendar.DAY_MILLIS);

        //get gregorian date
        int gregorianDay = date.getGregorianDate(Calendar.DAY_OF_MONTH);
        mGregorianDateString = String.valueOf(gregorianDay);
        gregorianTextView.setText(mGregorianDateString);

        //check luna festival and solar date
        int index = date.getLunarFestival();
        if (index >= 0){
            mLunarDateString = String.valueOf(fomatter.getLunarFestivalName(index));
            lunarTextView.setTextColor(mResources.getColor(R.color.lunarFestival));
        }else{
            index = date.getGregorianFestival();
            if (index >= 0){
                mLunarDateString = String.valueOf(fomatter.getGregorianFestivalName(index));
                lunarTextView.setTextColor(mResources.getColor(R.color.gregorianFestival));
            }else if (date.getLunar(LunarCalendar.LUNAR_DAY) == 1){
                mLunarDateString = String.valueOf(fomatter.getMonthName(date));
            }else if(gregorianDay == solarTerm1){
                mLunarDateString = String.valueOf(fomatter.getSolarTermName(date.getGregorianDate(Calendar.MONTH) * 2));
                lunarTextView.setTextColor(mResources.getColor(R.color.solarTerm));
            }else if(gregorianDay == solarTerm2){
                mLunarDateString = String.valueOf(fomatter.getSolarTermName(date.getGregorianDate(Calendar.MONTH) * 2 + 1));
                lunarTextView.setTextColor(mResources.getColor(R.color.solarTerm));
            }else{
                mLunarDateString = String.valueOf(fomatter.getDayName(date));
            }
        }
        lunarTextView.setText(mLunarDateString);

        //check if it is in current month
        //1. check first 7 dates, if large than 7, then must belong to last month
        //2. check last 14 dates (since there are 42 items per page), if less than 15, then must belong to next month
        boolean isOutOfRange = ((mPosition < 7 && gregorianDay > 7) || (mPosition > 27 && gregorianDay < 2 * 7 + 1));
        if (isOutOfRange) {
            gregorianTextView.setTextColor(mResources.getColor(R.color.outOfRange));
            lunarTextView.setTextColor(mResources.getColor(R.color.outOfRange));
        }

        //check weekends
        if (!isOutOfRange && (mPosition % 7 == 0 || mPosition % 7 == 6)) {
            gregorianTextView.setTextColor(mResources.getColor(R.color.weekends));
        }

        return convertView;
    }

}
