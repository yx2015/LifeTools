package com.yx.lifetools.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.yx.baselibrary.base.BaseAppCompatActivity;
import com.yx.baselibrary.eventbus.EventCenter;
import com.yx.baselibrary.utils.NetUtils;
import com.yx.lifetools.R;

/**
 * @ClassName BaseActivity
 * @Description activity基类
 * Created by yx on 2017-02-27.
 */
public abstract class BaseActivity extends BaseAppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus(R.color.Green);
    }

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected void onEventComming(EventCenter eventCenter) {

    }

    @Override
    protected void onNetworkConnected(NetUtils.NetType var1) {
        showToast("网络已连接");
    }


    @Override
    protected void onNetworkDisConnected() {
        showToast("网络已断开连接");
    }
}
