package com.yx.baselibrary.utils;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yx.baselibrary.R;

/**
 * @ClassName ProgressDialogUtil
 * @Description 进度条工具
 * Created by yx on 2017-02-28.
 */

public class ProgressDialogUtil {
    /**
     * 加载数据对话框
     */
    private static Dialog mLoadingDialog;

    /**
     * 显示加载对话框
     *
     * @param context    上下文
     * @param msg        对话框显示内容
     * @param cancelable 对话框是否可以取消
     */
    public static Dialog showLoadingDialog(Activity context, String msg, boolean cancelable) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_progress_dialog, null);
        TextView loadingText = (TextView) view.findViewById(R.id.loading_msg);
        loadingText.setText(msg);

        mLoadingDialog = new Dialog(context, R.style.CustomProgressDialog);
        mLoadingDialog.setCancelable(cancelable);
        mLoadingDialog.setCanceledOnTouchOutside(false);
        mLoadingDialog.setContentView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        return mLoadingDialog;
    }

    /**
     * 显示加载对话框
     *
     * @param context 上下文
     */
    public static Dialog showLoadingDialog(Activity context) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_progress_dialog, null);
        TextView loadingText = (TextView) view.findViewById(R.id.loading_msg);
        loadingText.setText("正在加载");

        mLoadingDialog = new Dialog(context, R.style.CustomProgressDialog);
        mLoadingDialog.setCancelable(true);
        mLoadingDialog.setCanceledOnTouchOutside(false);
        mLoadingDialog.setContentView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        return mLoadingDialog;
    }



    /**
     * 关闭加载对话框
     */
    public static void cancleLoadingDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.cancel();
        }
    }
}
