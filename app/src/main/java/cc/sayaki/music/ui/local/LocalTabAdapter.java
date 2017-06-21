package cc.sayaki.music.ui.local;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Author: sayaki
 * Date: 2017/6/16
 */
public class LocalTabAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;
    private List<String> titles;

    public LocalTabAdapter(List<Fragment> fragments, List<String> titles, FragmentManager fm) {
        super(fm);
        this.fragments = fragments;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}
