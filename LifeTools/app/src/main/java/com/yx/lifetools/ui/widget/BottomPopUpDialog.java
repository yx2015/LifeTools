package com.yx.lifetools.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.yx.lifetools.R;
import com.yx.lifetools.bean.CommunityInfo;
import com.yx.lifetools.ui.a.CommunityAdpater;

import java.util.List;

/**
 * @ClassName BottomPopUpDialog
 * @Description 底部弹出框
 * Created by yx on 2017-03-09.
 */

public class BottomPopUpDialog {
    private Context context;
    private Dialog dialog;
    private Display display;
    private FrameLayout contentView;
    private List<CommunityInfo> datas;

    public BottomPopUpDialog(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
        this.context = context;
    }

    public BottomPopUpDialog builder() {
        View view = LayoutInflater.from(context).inflate(R.layout.ui_bottom_pop_up_dialog, null);
        ImageView ic_close = (ImageView) view.findViewById(R.id.ic_close);
        ic_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        // 获取自定义Dialog布局中的控件
        contentView = (FrameLayout) view.findViewById(R.id.fl_content);
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        // 设置Dialog最小宽度为屏幕宽度
        view.setMinimumWidth(display.getWidth());

        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.ActionBottomDialogStyle);
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        dialogWindow.setAttributes(lp);
        return this;
    }

    /**
     * 对话框是否显示
     *
     * @return
     */
    public boolean isShowing() {
        return dialog.isShowing();
    }

    public BottomPopUpDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    /**
     * 设置外部点击
     *
     * @param cancel
     * @return
     */
    public BottomPopUpDialog setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }

    public void show() {
        dialog.show();
    }

    public interface OnSheetItemClickListener {
        void onClick(int which);
    }


    public void dismiss() {
        dialog.dismiss();
    }


    /**
     * @param strItems
     * @param listener
     * @param count
     * @return BottomPopUpDialog
     * @Title: addSheetItem
     * @Description: 添加条目
     */
    public BottomPopUpDialog addSheetItem(List<CommunityInfo> strItems, final OnSheetItemClickListener listener, int count) {
        int size = strItems.size();
        datas = strItems;
        // 添加条目过多的时候控制高度
        if (size >= 6) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) contentView.getLayoutParams();
            params.width = display.getWidth();
            params.height = display.getHeight();
            contentView.setLayoutParams(params);
        }
        View view = View.inflate(context, R.layout.ui_dialog_item, null);
        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, count);
        CommunityAdpater adpater = new CommunityAdpater(datas, context);
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                listener.onClick(position);
            }
        });
        mRecyclerView.setAdapter(adpater);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        contentView.addView(view);
        return this;
    }

}
