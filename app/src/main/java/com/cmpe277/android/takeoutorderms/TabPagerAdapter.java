package com.cmpe277.android.takeoutorderms;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by weiyao on 4/14/18.
 */

public class TabPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public TabPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new AddItemTab();
            case 1:
                return new ViewItemTab();
            case 2:
                return new OrderReportTab();
        }
        return null;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    @Override
    public int getItemPosition(Object object){
        return POSITION_NONE;
    }
}
