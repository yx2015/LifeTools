package com.yx.lifetools.ui.activity;

import android.view.View;
import android.widget.RadioButton;

import com.yx.baselibrary.base.BaseLazyFragment;
import com.yx.baselibrary.widget.XViewPager;
import com.yx.lifetools.R;
import com.yx.lifetools.ui.adapter.VPFragmentAdapter;
import com.yx.lifetools.ui.fragment.CommunityNewFragment;
import com.yx.lifetools.ui.fragment.MainFragment;
import com.yx.lifetools.ui.fragment.MyFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

public class MainNewActivity extends BaseActivity implements View.OnClickListener {

    @InjectView(R.id.home_container)
    XViewPager mViewPager;
    @InjectView(R.id.rb_main)
    RadioButton mMain;


    @InjectView(R.id.rb_community)
    RadioButton mCommunity;


    @InjectView(R.id.rb_my)
    RadioButton mMine;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_main_new;
    }

    @Override
    protected void initViewsAndEvents() {
        List<BaseLazyFragment> fragments = new ArrayList<>();
        fragments.add(new MainFragment());
        fragments.add(new CommunityNewFragment());
        fragments.add(new MyFragment());

        if (!fragments.isEmpty()) {
            mViewPager.setOffscreenPageLimit(fragments.size());
            mViewPager.setEnableScroll(false);
            mViewPager.setAdapter(new VPFragmentAdapter(getSupportFragmentManager(), fragments));
        }
        mViewPager.setCurrentItem(1, false);

        mMain.setOnClickListener(this);
        mCommunity.setOnClickListener(this);
        mMine.setOnClickListener(this);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rb_main:
                mViewPager.setCurrentItem(0, false);
                break;
            case R.id.rb_community:
                mViewPager.setCurrentItem(1, false);
                break;
            case R.id.rb_my:
                mViewPager.setCurrentItem(2, false);
                break;

        }
    }
}
