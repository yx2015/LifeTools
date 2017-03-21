package com.yx.baselibrary.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.yx.baselibrary.observer.NetChangeObserver;
import com.yx.baselibrary.utils.NetUtils;
import com.yx.baselibrary.utils.TLogUtil;

import java.util.ArrayList;

/**
 * @ClassName NetStateReceiver
 * @Description Created by yx on 2017-02-28.
 */

public class NetStateReceiver extends BroadcastReceiver {
    public static final String CUSTOM_ANDROID_NET_CHANGE_ACTION = "com.github.obsessive.library.net.conn.CONNECTIVITY_CHANGE";
    private static final String ANDROID_NET_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
    private static final String TAG = NetStateReceiver.class.getSimpleName();
    private static boolean isNetAvailable = false;
    private static NetUtils.NetType mNetType;
    private static ArrayList<NetChangeObserver> mNetChangeObservers = new ArrayList<>();
    private static BroadcastReceiver mBroadcastReceiver;

    public NetStateReceiver() {
    }

    private static BroadcastReceiver getReceiver() {
        if (mBroadcastReceiver == null) {
            mBroadcastReceiver = new NetStateReceiver();
        }

        return mBroadcastReceiver;
    }

    public void onReceive(Context context, Intent intent) {
        mBroadcastReceiver = this;
        if (intent.getAction().equalsIgnoreCase("android.net.conn.CONNECTIVITY_CHANGE") || intent.getAction().equalsIgnoreCase("com.github.obsessive.library.net.conn.CONNECTIVITY_CHANGE")) {
            if (!NetUtils.isNetworkAvailable(context)) {
                TLogUtil.i(TAG, "<--- network disconnected --->");
                isNetAvailable = false;
            } else {
                TLogUtil.i(TAG, "<--- network connected --->");
                isNetAvailable = true;
                mNetType = NetUtils.getAPNType(context);
            }

            this.notifyObserver();
        }

    }

    public static void registerNetworkStateReceiver(Context mContext) {
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.github.obsessive.library.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        mContext.getApplicationContext().registerReceiver(getReceiver(), filter);
    }

    public static void checkNetworkState(Context mContext) {
        Intent intent = new Intent();
        intent.setAction("com.github.obsessive.library.net.conn.CONNECTIVITY_CHANGE");
        mContext.sendBroadcast(intent);
    }

    public static void unRegisterNetworkStateReceiver(Context mContext) {
        if (mBroadcastReceiver != null) {
            try {
                mContext.getApplicationContext().unregisterReceiver(mBroadcastReceiver);
            } catch (Exception var2) {
                TLogUtil.d(TAG, var2.getMessage());
            }
        }

    }

    public static boolean isNetworkAvailable() {
        return isNetAvailable;
    }

    public static NetUtils.NetType getAPNType() {
        return mNetType;
    }

    private void notifyObserver() {
        if (!mNetChangeObservers.isEmpty()) {
            int size = mNetChangeObservers.size();

            for (int i = 0; i < size; ++i) {
                NetChangeObserver observer = (NetChangeObserver) mNetChangeObservers.get(i);
                if (observer != null) {
                    if (isNetworkAvailable()) {
                        observer.onNetConnected(mNetType);
                    } else {
                        observer.onNetDisConnect();
                    }
                }
            }
        }

    }

    public static void registerObserver(NetChangeObserver observer) {
        if (mNetChangeObservers == null) {
            mNetChangeObservers = new ArrayList();
        }

        mNetChangeObservers.add(observer);
    }

    public static void removeRegisterObserver(NetChangeObserver observer) {
        if (mNetChangeObservers != null && mNetChangeObservers.contains(observer)) {
            mNetChangeObservers.remove(observer);
        }

    }
}
