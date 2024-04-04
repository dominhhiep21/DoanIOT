package com.example.doaniot.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.doaniot.Fragment.Fragment_chart;
import com.example.doaniot.Fragment.Fragment_today;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private int pagerNumber;
    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.pagerNumber=behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new Fragment_today();
            case 1: return new Fragment_chart();
        }
        return null;
    }

    @Override
    public int getCount() {
        return this.pagerNumber;
    }
}
