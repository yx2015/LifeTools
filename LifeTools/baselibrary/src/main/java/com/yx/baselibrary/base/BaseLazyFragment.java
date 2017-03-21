package com.yx.baselibrary.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yx.baselibrary.eventbus.EventCenter;
import com.yx.baselibrary.utils.ProgressDialogUtil;
import com.yx.baselibrary.utils.ToastUitl;

import java.lang.reflect.Field;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * @ClassName BaseLazyFragment
 * @Description fragment基类
 * Created by yx on 2017-02-27.
 */

public abstract class BaseLazyFragment extends Fragment {
    private boolean isFirstResume = true;
    private boolean isFirstVisible = true;
    private boolean isFirstInvisible = true;
    private boolean isPrepared;
    protected static String TAG_LOG = null;

    public BaseLazyFragment() {
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG_LOG = this.getClass().getSimpleName();
        if (this.isBindEventBusHere()) {
            EventBus.getDefault().register(this);
        }
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return this.getContentViewLayoutID() != 0 ? inflater.inflate(this.getContentViewLayoutID(), (ViewGroup) null) : super.onCreateView(inflater, container, savedInstanceState);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        this.initViewsAndEvents();
    }


    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.isBindEventBusHere()) {
            EventBus.getDefault().unregister(this);
        }

    }


    public void onDetach() {
        super.onDetach();
        try {
            Field var3 = Fragment.class.getDeclaredField("mChildFragmentManager");
            var3.setAccessible(true);
            var3.set(this, (Object) null);
        } catch (NoSuchFieldException var2) {
            throw new RuntimeException(var2);
        } catch (IllegalAccessException var31) {
            throw new RuntimeException(var31);
        }
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.initPrepare();
    }

    public void onResume() {
        super.onResume();
        if (this.isFirstResume) {
            this.isFirstResume = false;
        } else if (this.getUserVisibleHint()) {
            this.onUserVisible();
        }

    }

    public void onPause() {
        super.onPause();
        if (this.getUserVisibleHint()) {
            this.onUserInvisible();
        }

    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (this.isFirstVisible) {
                this.isFirstVisible = false;
                this.initPrepare();
            } else {
                this.onUserVisible();
            }
        } else if (this.isFirstInvisible) {
            this.isFirstInvisible = false;
            this.onFirstUserInvisible();
        } else {
            this.onUserInvisible();
        }

    }

    private synchronized void initPrepare() {
        if (this.isPrepared) {
            this.onFirstUserVisible();
        } else {
            this.isPrepared = true;
        }

    }

    protected abstract void onFirstUserVisible();

    protected abstract void onUserVisible();

    private void onFirstUserInvisible() {
    }

    protected abstract void onUserInvisible();

    protected abstract View getLoadingTargetView();

    protected abstract void initViewsAndEvents();

    protected abstract int getContentViewLayoutID();

    protected abstract void onEventComming(EventCenter var1);

    protected abstract boolean isBindEventBusHere();

    protected FragmentManager getSupportFragmentManager() {
        return this.getActivity().getSupportFragmentManager();
    }

    protected void readyGo(Class<?> clazz) {
        Intent intent = new Intent(this.getActivity(), clazz);
        this.startActivity(intent);
    }

    protected void readyGo(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(this.getActivity(), clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }

        this.startActivity(intent);
    }

    protected void readyGoForResult(Class<?> clazz, int requestCode) {
        Intent intent = new Intent(this.getActivity(), clazz);
        this.startActivityForResult(intent, requestCode);
    }

    protected void readyGoForResult(Class<?> clazz, int requestCode, Bundle bundle) {
        Intent intent = new Intent(this.getActivity(), clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }

        this.startActivityForResult(intent, requestCode);
    }

    protected void showToast(String msg) {
        if (null != msg && !TextUtils.isEmpty(msg)) {
            ToastUitl.showShort(msg);
        }

    }

    public void onEventMainThread(EventCenter eventCenter) {
        if (null != eventCenter) {
            this.onEventComming(eventCenter);
        }

    }

    /**
     * 开启加载进度条
     */
    public void startProgressDialog() {
        ProgressDialogUtil.showLoadingDialog(getActivity()).show();
    }

    /**
     * 开启加载进度条
     *
     * @param msg
     */
    public void startProgressDialog(String msg) {
        ProgressDialogUtil.showLoadingDialog(getActivity(), msg, true).show();
    }

    /**
     * 停止加载进度条
     */
    public void stopProgressDialog() {
        ProgressDialogUtil.cancleLoadingDialog();
    }
}
