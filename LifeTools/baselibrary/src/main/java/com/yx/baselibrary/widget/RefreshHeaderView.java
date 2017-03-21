package com.yx.baselibrary.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.lcodecore.tkrefreshlayout.IHeaderView;
import com.lcodecore.tkrefreshlayout.OnAnimEndListener;
import com.yx.baselibrary.R;

/**
 * @ClassName RefreshViewHeader
 * @Description 刷新头部栏
 * Created by yx on 2017-02-28.
 */

public class RefreshHeaderView extends FrameLayout implements IHeaderView {
    ImageView refreshArrow;
    TextView refreshTextView;
    private TextView mSubHeaderText;
    private ImageView mImgPerson;
    private ImageView mImgGoods;
    private View headerView;
    public static final int PULL_TO_REFRESH = 0;
    private AnimationDrawable animP;

    public RefreshHeaderView(Context context) {
        this(context, null);
    }

    public RefreshHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (headerView == null) {
            headerView = View.inflate(getContext(), R.layout.refresh_header_layout, null);
            mSubHeaderText = (TextView) headerView.findViewById(R.id.pull_to_refresh_sub_text);
            mImgPerson = (ImageView) headerView.findViewById(R.id.imageView2);
            mImgGoods = (ImageView) headerView.findViewById(R.id.imageView1);
        }
        addView(headerView);

    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void onPullingDown(float fraction, float maxHeadHeight, float headHeight) {
        if (fraction < 1f) mSubHeaderText.setText("下拉刷新");
        if (fraction > 1f) mSubHeaderText.setText("释放刷新");

    }

    @Override
    public void onPullReleasing(float fraction, float maxHeadHeight, float headHeight) {
        if (fraction < 1f) {
            mSubHeaderText.setText("下拉刷新");
        }
    }

    @Override
    public void startAnim(float maxHeadHeight, float headHeight) {
        mSubHeaderText.setText("正在刷新");
        if (animP == null) {
            mImgPerson.setImageResource(R.drawable.refresh_anim);
            animP = (AnimationDrawable) mImgPerson.getDrawable();
        }
        animP.start();
        mImgGoods.setVisibility(INVISIBLE);

    }

    @Override
    public void onFinish(OnAnimEndListener animEndListener) {
        animEndListener.onAnimEnd();
        mImgGoods.setVisibility(VISIBLE);
        if (animP != null) {
            animP.stop();
            animP = null;
        }
    }

    @Override
    public void reset() {
        mSubHeaderText.setText("下拉刷新");
    }
}
