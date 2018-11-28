package com.zhuochi.hydream.adapter;




import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by ztech on 2018/7/5.
 */

public class RankVPFragmentAdapter extends FragmentStatePagerAdapter {
    List<Fragment> listfragment;
    List<String> listtitle;

    public RankVPFragmentAdapter(FragmentManager fm, List<Fragment> listfragment, List<String> listtitle) {
        super(fm);
        this.listfragment = listfragment;
        this.listtitle = listtitle;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return listtitle.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return listfragment.get(position);
    }

    @Override
    public int getCount() {
        return listfragment.size();
    }
}
