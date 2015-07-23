package com.calendar.feideng.flunarcalendar;

import android.content.res.Resources;

import com.calendar.feideng.module.DateFormatter;
import com.calendar.feideng.module.LunarCalendar;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by fdeng on 5/13/15.
 */
public class CalendarCell {

    private long firstDayMillis = 0;
    private int solarTerm1 = 0;
    private int solarTerm2 = 0;
    private DateFormatter fomatter;

    private String mGregorianDateString = "";
    private String mLunarDateString = "";
    private int mPosition = 0;
    public CalendarCell(Resources resources, int monthIndex, int position) {
        int year = LunarCalendar.getMinYear() + (monthIndex / 12);
        int month = monthIndex % 12;
        Calendar date = new GregorianCalendar(year, month, 1);
        int offset = 1 - date.get(Calendar.DAY_OF_WEEK);
        date.add(Calendar.DAY_OF_MONTH, offset);
        firstDayMillis = date.getTimeInMillis();
        solarTerm1 = LunarCalendar.getSolarTerm(year, month * 2 + 1);
        solarTerm2 = LunarCalendar.getSolarTerm(year, month * 2 + 2);
        fomatter = new DateFormatter(resources);
        mPosition = position;
        analyzeDate(mPosition);
    }

    private void analyzeDate(int position) {
        LunarCalendar date = new LunarCalendar(firstDayMillis + position * LunarCalendar.DAY_MILLIS);

        //get gregorian date
        int gregorianDay = date.getGregorianDate(Calendar.DAY_OF_MONTH);
        mGregorianDateString = String.valueOf(gregorianDay);


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
            isFestival = true;
        }else{
            index = date.getGregorianFestival();
            if (index >= 0){
                // 公历节日
                mLunarDateString = String.valueOf(fomatter.getGregorianFestivalName(index));
                isFestival = true;
            }else if (date.getLunar(LunarCalendar.LUNAR_DAY) == 1){
                // 初一,显示月份
                mLunarDateString = String.valueOf(fomatter.getMonthName(date));
            }else if(!isOutOfRange && gregorianDay == solarTerm1){
                // 节气1
                mLunarDateString = String.valueOf(fomatter.getSolarTermName(date.getGregorianDate(Calendar.MONTH) * 2));
                isSolarTerm = true;
            }else if(!isOutOfRange && gregorianDay == solarTerm2){
                // 节气2
                mLunarDateString = String.valueOf(fomatter.getSolarTermName(date.getGregorianDate(Calendar.MONTH) * 2 + 1));
                isSolarTerm = true;
            }else{
                mLunarDateString = String.valueOf(fomatter.getDayName(date));
            }
        }
    }

    public String getGregorianDateString() {
        return mGregorianDateString;
    }

    public String getLunarDateString() {
        return mLunarDateString;
    }

}
