package com.example.nitin.miwok;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.nitin.miwok.ColorsFragment;
import com.example.nitin.miwok.FamilyFragment;
import com.example.nitin.miwok.NumbersFragment;
import com.example.nitin.miwok.PhrasesFragment;

/**
 * Created by nitin on 12/1/18.
 */

public class SimplePagerAdapter extends FragmentPagerAdapter {
    String Titles[]=new String[]{"Numbers","Family","Colors","Phrases"};
    public SimplePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position==0){
            return new NumbersFragment();
        }
        else if (position==1){
            return new FamilyFragment();
        }
        else if (position==2){
            return new ColorsFragment();
        }
        else{
            return new PhrasesFragment();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    @Override
    public int getCount() {
        return 4;
    }
}
