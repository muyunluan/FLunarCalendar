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
        int year = LunarCalendar.getMinYear() + (monthIndex / 12);
        int month = monthIndex % 12;
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
//        cell = new CalendarCell(mResources, mMonthIndex, mPosition);
//        //Log.i("", mPosition + "- month index: " + mMonthIndex);
//
//        gregorianTextView.setText(cell.getGregorianDateString());
//        lunarTextView.setText(cell.getLunarDateString());

        //check weekends
        if (mPosition % 7 == 0 || mPosition % 7 == 6) {
            gregorianTextView.setTextColor(mResources.getColor(R.color.weekends));
        }
        LunarCalendar date = new LunarCalendar(firstDayMillis + position * LunarCalendar.DAY_MILLIS);

        //get gregorian date
        int gregorianDay = date.getGregorianDate(Calendar.DAY_OF_MONTH);
        mGregorianDateString = String.valueOf(gregorianDay);
        gregorianTextView.setText(mGregorianDateString);

        String dateStr = "";
        if(position % 8 == 0) {
            dateStr = String.valueOf(date.getGregorianDate(Calendar.WEEK_OF_YEAR));
        }
        // 开始日期处理
        boolean isFestival = false, isSolarTerm = false;

        // 判断是否为本月日期
        boolean isOutOfRange = ((position % 8 != 0) &&
                (position < 8 && gregorianDay > 7) || (position > 8 && gregorianDay < position - 7 - 6));


        // 农历节日 > 公历节日 > 农历月份 > 二十四节气 > 农历日
        int index = date.getLunarFestival();
        if (index >= 0){
            // 农历节日
            mLunarDateString = String.valueOf(fomatter.getLunarFestivalName(index));
            lunarTextView.setTextColor(mResources.getColor(R.color.lunarFestival));
            isFestival = true;
        }else{
            index = date.getGregorianFestival();
            if (index >= 0){
                // 公历节日
                mLunarDateString = String.valueOf(fomatter.getGregorianFestivalName(index));
                lunarTextView.setTextColor(mResources.getColor(R.color.gregorianFestival));
                isFestival = true;
            }else if (date.getLunar(LunarCalendar.LUNAR_DAY) == 1){
                // 初一,显示月份
                mLunarDateString = String.valueOf(fomatter.getMonthName(date));
            }else if(!isOutOfRange && gregorianDay == solarTerm1){
                // 节气1
                mLunarDateString = String.valueOf(fomatter.getSolarTermName(date.getGregorianDate(Calendar.MONTH) * 2));
                lunarTextView.setTextColor(mResources.getColor(R.color.solarTerm));
                isSolarTerm = true;
            }else if(!isOutOfRange && gregorianDay == solarTerm2){
                // 节气2
                mLunarDateString = String.valueOf(fomatter.getSolarTermName(date.getGregorianDate(Calendar.MONTH) * 2 + 1));
                lunarTextView.setTextColor(mResources.getColor(R.color.solarTerm));
                isSolarTerm = true;
            }else{
                mLunarDateString = String.valueOf(fomatter.getDayName(date));
            }
        }
        lunarTextView.setText(mLunarDateString);

        return convertView;
    }

}
