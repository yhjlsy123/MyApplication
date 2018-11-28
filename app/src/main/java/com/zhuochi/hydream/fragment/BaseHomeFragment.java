package com.zhuochi.hydream.fragment;

import com.zhuochi.hydream.base.BaseFragment;

/**
 * Created by and on 2016/11/12.
 */

public class BaseHomeFragment extends BaseFragment {
    private HomeContent homeContent;

    public void binding(HomeContent homeContent) {
        this.homeContent = homeContent;
    }

    /**
     * 是否绑定homefragment
     * @return
     */
    public boolean bindresult() {
        if (homeContent != null)
            return true;
        return false;
    }

    public HomeContent getHomeContent() {
        return homeContent;
    }
}
