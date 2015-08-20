package com.calendar.feideng.flunarcalendar;


import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.calendar.feideng.module.LunarCalendar;

import java.util.Calendar;

public class MainActivity extends FragmentActivity {

    private ViewPager viewPager;
    private CalendarViewPagerAdapter calendarViewPagerAdapter;
    private int todayMonthIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configure action bar
        ActionBar actionBar = getActionBar();
        // Enable navigating up with app icon
        actionBar.setDisplayHomeAsUpEnabled(false);
        // Disable app icon
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        //actionBar.setTitle("My Profile");
        actionBar.setDisplayUseLogoEnabled(false);

        initView();
    }

    // Init ViewPager
    public void initView() {
        viewPager = (ViewPager)findViewById(R.id.id_viewpager);
        // Declare an Adapter for ViewPager
        calendarViewPagerAdapter = new CalendarViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(calendarViewPagerAdapter);
        todayMonthIndex = getTodayMonthIndex();
        viewPager.setCurrentItem(todayMonthIndex);
    }

    private int getTodayMonthIndex() {
        Calendar today = Calendar.getInstance();
        int offset = (today.get(Calendar.YEAR) - LunarCalendar.getMinYear())
                * 12 + today.get(Calendar.MONTH);
        return offset;
    }

    // ViewPager adapter used to talk with Fragments
    public class CalendarViewPagerAdapter extends FragmentPagerAdapter {

        public CalendarViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            //Log.i("", "current page: " + position);
            return MonthFragment.create(position);
        }

        @Override
        public int getCount() {
            int years = LunarCalendar.getMaxYear() - LunarCalendar.getMinYear();
            return years * 12;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_today:
                viewPager.setCurrentItem(todayMonthIndex);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /* Click actions for bottom textView */
    /*
    public void onTodayClick(View view) {
        Toast.makeText(MainActivity.this, "Today click", Toast.LENGTH_LONG).show();
    }

    public void onCalendarsClick(View view) {
        Toast.makeText(MainActivity.this, "Calendars click", Toast.LENGTH_LONG).show();
    }

    public void onInboxClick(View view) {
        Toast.makeText(MainActivity.this, "Inbox click", Toast.LENGTH_LONG).show();
    }
    */
}
