package com.yx.baselibrary.utils;

import android.graphics.Color;
import android.widget.Toast;

import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.yx.baselibrary.base.BaseLibraryApplication;

/**
 * @ClassName ToastUitl
 * @Description 吐司工具
 * Created by yx on 2017-02-27.
 */

public class ToastUitl {

    private static StyleableToast st;

    /**
     * showShort 吐司
     *
     * @param msg
     */
    public static void showShort(String msg) {
        if (st == null) {
            st = new StyleableToast(BaseLibraryApplication.getAppContext());
            st.setTextColor(Color.WHITE);
            st.setBackgroundColor(Color.parseColor("#ff5a5f"));
            st.setMaxAlpha();
            st.setDuration(Toast.LENGTH_SHORT);
            st.setIcon(android.R.drawable.ic_dialog_alert);
        }
        st.setToastMsg(msg);
        st.show();
    }
}
