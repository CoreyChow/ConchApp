package com.isoftston.issuser.conchapp.views.work.adpter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.isoftston.issuser.conchapp.views.work.ItemFragment;

/**
 * Created by issuser on 2018/4/16.
 */

public class WorkMessageAdapter  extends FragmentPagerAdapter {
    private String[] mTitles;

    public WorkMessageAdapter(FragmentManager fm, String[] mTitles){

        super(fm);
        this.mTitles=mTitles;
    }

    public void setmTitles(String[] mTitles){
        this.mTitles=mTitles;
    }

    @Override
    public Fragment getItem(int position) {
        return ItemFragment.newInstance(mTitles[position]);
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}