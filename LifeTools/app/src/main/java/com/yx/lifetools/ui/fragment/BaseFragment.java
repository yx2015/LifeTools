package com.yx.lifetools.ui.fragment;

import com.yx.baselibrary.base.BaseLazyFragment;
import com.yx.baselibrary.eventbus.EventCenter;

/**
 * @ClassName BaseFragment
 * @Description fragment基类
 * Created by yx on 2017-02-27.
 */

public abstract class BaseFragment extends BaseLazyFragment {
    @Override
    protected void onEventComming(EventCenter var1) {

    }

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }


    @Override
    protected void onFirstUserVisible() {

    }

    @Override
    protected void onUserVisible() {

    }

    @Override
    protected void onUserInvisible() {

    }
}
