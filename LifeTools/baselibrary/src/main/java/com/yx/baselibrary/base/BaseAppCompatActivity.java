package com.yx.baselibrary.base;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;

import com.yx.baselibrary.eventbus.EventCenter;
import com.yx.baselibrary.observer.NetChangeObserver;
import com.yx.baselibrary.receiver.NetStateReceiver;
import com.yx.baselibrary.utils.NetUtils;
import com.yx.baselibrary.utils.ProgressDialogUtil;
import com.yx.baselibrary.utils.SmartBarUtils;
import com.yx.baselibrary.utils.StatusBarUtil;
import com.yx.baselibrary.utils.ToastUitl;
import com.zhy.autolayout.AutoLayoutActivity;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

/**
 * @ClassName BaseAppCompatActivity
 * @Description activity基类
 * Created by yx on 2017-01-09.
 */
@RuntimePermissions
public abstract class BaseAppCompatActivity extends AutoLayoutActivity {
    private PermissionHandler mHandler;
    protected NetChangeObserver mNetChangeObserver = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*添加到管理类*/
        BaseAppManager.getAppManager().addActivity(this);

        // 无标题
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        // 设置竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // 默认软键盘不弹出
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        /*注册eventbus监听事件*/
        if (this.isBindEventBusHere()) {
            EventBus.getDefault().register(this);
        }

        /*解决魅族适配问题*/
        SmartBarUtils.hide(this.getWindow().getDecorView());

        if (this.getContentViewLayoutID() != 0) {
            /*填充布局*/
            this.setContentView(this.getContentViewLayoutID());
            //注册网络监听
//            NetStateReceiver.registerNetworkStateReceiver(this);
            this.mNetChangeObserver = new NetChangeObserver() {
                public void onNetConnected(NetUtils.NetType type) {
                    super.onNetConnected(type);
                    BaseAppCompatActivity.this.onNetworkConnected(type);
                }

                public void onNetDisConnect() {
                    super.onNetDisConnect();
                    BaseAppCompatActivity.this.onNetworkDisConnected();
                }
            };
            NetStateReceiver.registerObserver(mNetChangeObserver);
            this.initViewsAndEvents();
        } else {
            throw new IllegalArgumentException("You must return a right contentView layout resource Id");
        }


    }

    /**
     * 着色状态栏（4.4以上系统有效）
     */
    public void setTranslucentStatus(int color) {
        StatusBarUtil.setTranslucentStatus(this, color);
    }


    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.inject(this);
    }

    protected abstract int getContentViewLayoutID();

    protected abstract boolean isBindEventBusHere();

    protected abstract void onEventComming(EventCenter eventCenter);

    protected abstract void initViewsAndEvents();

    protected abstract void onNetworkConnected(NetUtils.NetType var1);

    protected abstract void onNetworkDisConnected();

    protected void readyGo(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        this.startActivity(intent);
    }

    protected void readyGo(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        this.startActivity(intent);
    }

    protected void readyGoThenKill(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        this.startActivity(intent);
        this.finish();
    }

    protected void readyGoThenKill(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        this.startActivity(intent);
        this.finish();
    }

    protected void readyGoForResult(Class<?> clazz, int requestCode) {
        Intent intent = new Intent(this, clazz);
        this.startActivityForResult(intent, requestCode);
    }

    protected void readyGoForResult(Class<?> clazz, int requestCode, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }

        this.startActivityForResult(intent, requestCode);
    }

    /**
     * 开启加载进度条
     */
    public void startProgressDialog() {
        ProgressDialogUtil.showLoadingDialog(this).show();
    }

    /**
     * 开启加载进度条
     *
     * @param msg
     */
    public void startProgressDialog(String msg) {
        ProgressDialogUtil.showLoadingDialog(this, msg, true).show();
    }

    /**
     * 停止加载进度条
     */
    public void stopProgressDialog() {
        ProgressDialogUtil.cancleLoadingDialog();
    }

    @Override
    public void finish() {
        super.finish();
        BaseAppManager.getAppManager().finishActivity(this);
    }

    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.reset(this);
//        NetStateReceiver.unRegisterNetworkStateReceiver(this);
        NetStateReceiver.removeRegisterObserver(this.mNetChangeObserver);
        if (this.isBindEventBusHere()) {
            EventBus.getDefault().unregister(this);
        }

    }

    public void onEventMainThread(EventCenter eventCenter) {
        if (null != eventCenter) {
            this.onEventComming(eventCenter);
        }
    }

    /**
     * 权限回调接口
     */
    public abstract class PermissionHandler {
        /**
         * 权限通过
         */
        public abstract void onGranted();

        /**
         * 权限拒绝
         */
        public void onDenied() {
            showToast("请打开权限，以便功能正常使用");
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        BaseAppCompatActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
    //-----------------------------------------------------------

    /**
     * 请求相机权限
     *
     * @param permissionHandler
     */
    protected void requestCameraPermission(PermissionHandler permissionHandler) {
        this.mHandler = permissionHandler;
        BaseAppCompatActivityPermissionsDispatcher.handleCameraPermissionWithCheck(this);
    }


    @NeedsPermission(Manifest.permission.CAMERA)
    void handleCameraPermission() {
        if (mHandler != null)
            mHandler.onGranted();
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    void deniedCameraPermission() {
        if (mHandler != null)
            mHandler.onDenied();
    }

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    void OnCameraNeverAskAgain() {
        showDialog("[相机]");
    }

    //-----------------------------------------------------------

    /**
     * 请求电话权限
     *
     * @param permissionHandler
     */
    protected void requestCallPermission(PermissionHandler permissionHandler) {
        this.mHandler = permissionHandler;
        BaseAppCompatActivityPermissionsDispatcher.handleCallPermissionWithCheck(this);
    }


    @NeedsPermission(Manifest.permission.CALL_PHONE)
    void handleCallPermission() {
        if (mHandler != null)
            mHandler.onGranted();
    }

    @OnPermissionDenied(Manifest.permission.CALL_PHONE)
    void deniedCallPermission() {
        if (mHandler != null)
            mHandler.onDenied();
    }

    @OnNeverAskAgain(Manifest.permission.CALL_PHONE)
    void OnCallNeverAskAgain() {
        showDialog("[电话]");
    }

    //-----------------------------------------------------------

    /**
     * 请求读写SD卡权限
     *
     * @param permissionHandler
     */
    protected void requestReadAndWriteSDPermission(PermissionHandler permissionHandler) {
        this.mHandler = permissionHandler;
        BaseAppCompatActivityPermissionsDispatcher.handleReadAndWriteSDPermissionWithCheck(this);
    }


    @NeedsPermission(value = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void handleReadAndWriteSDPermission() {
        if (mHandler != null)
            mHandler.onGranted();
    }

    @OnPermissionDenied(value = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void deniedReadAndWriteSDPermission() {
        if (mHandler != null)
            mHandler.onDenied();
    }

    @OnNeverAskAgain(value = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void OnReadAndWriteSDNeverAskAgain() {
        showDialog("[存储空间]");
    }
    //-----------------------------------------------------------

    protected void showToast(String msg) {
        if (null != msg && !TextUtils.isEmpty(msg)) {
            ToastUitl.showShort(msg);
        }
    }

    /**
     * 请求位置信息权限
     *
     * @param permissionHandler
     */
    protected void requestLocationPermission(PermissionHandler permissionHandler) {
        this.mHandler = permissionHandler;
        BaseAppCompatActivityPermissionsDispatcher.handleLocationPermissionWithCheck(this);
    }


    @NeedsPermission(value = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void handleLocationPermission() {
        if (mHandler != null)
            mHandler.onGranted();
    }

    @OnPermissionDenied(value = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void deniedLocationPermission() {
        if (mHandler != null)
            mHandler.onDenied();
    }

    @OnNeverAskAgain(value = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void OnLocationNeverAskAgain() {
        showDialog("[位置信息]");
    }

    public void showDialog(String permission) {
        new AlertDialog.Builder(this).setTitle("权限申请").setMessage("在设置-应用-大众生活-权限中开启" + permission + "权限，以正常使用大众生活功能").setPositiveButton("去开启", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
                dialog.dismiss();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mHandler != null)
                    mHandler.onDenied();
                dialog.dismiss();
            }
        }).setCancelable(false).show();
    }

}
