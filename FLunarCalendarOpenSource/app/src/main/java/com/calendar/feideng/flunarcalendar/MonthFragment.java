package com.calendar.feideng.flunarcalendar;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class MonthFragment extends Fragment {

    /**
     * The argument key for the page number this fragment represents.
     */
    public static final String ARG_PAGE = "page";

    /**
     * The fragment's page number, which is set to the argument value for {@link #ARG_PAGE}.
     */
    private int mPageNumber;

    private MonthFragmentAdapter monthFragmentAdapter;

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static MonthFragment create(int pageNumber) {
        MonthFragment monthFragment = new MonthFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_PAGE, pageNumber);
        monthFragment.setArguments(bundle);
        return monthFragment;
    }


    public MonthFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.month_fragment_gridview, container, false);
        GridView gridView = (GridView)rootView.findViewById(R.id.id_month_gridview);
        monthFragmentAdapter = new MonthFragmentAdapter(rootView.getContext());
        gridView.setAdapter(monthFragmentAdapter);
        Calendar calendar = Calendar.getInstance();
        return rootView;
    }

    /**
     * Returns the page number represented by this fragment object.
     */
    public int getPageNumber() {
        return mPageNumber;
    }
}
