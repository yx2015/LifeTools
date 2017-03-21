package com.yx.lifetools.ui.a;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yx.lifetools.R;
import com.yx.lifetools.bean.CommunityInfo;

import java.util.List;

/**
 * Created by yx on 2017-03-09.
 */

public class CommunityAdpater extends BaseQuickAdapter<CommunityInfo, BaseViewHolder> {

    private ImageView mIcon;
    private TextView mName;
    private Context context;

    public CommunityAdpater(List<CommunityInfo> data, Context context) {
        super(R.layout.ui_community_item, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, CommunityInfo item) {
        mIcon = helper.getView(R.id.iv_icon);
        mName = helper.getView(R.id.tv_name);
        mName.setText(item.getName());
        mIcon.setImageResource(item.getIconResId());
    }
}
