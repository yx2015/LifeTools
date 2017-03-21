package com.yx.lifetools.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.yx.baselibrary.widget.RefreshHeaderView;
import com.yx.lifetools.R;
import com.yx.lifetools.bean.MainDto;
import com.yx.lifetools.ui.adapter.MainAdapter;

import java.util.List;

import butterknife.InjectView;

/**
 * Created by Administrator on 2017-03-07.
 */
public class MainFragment extends BaseFragment {
    @InjectView(R.id.twink)
    TwinklingRefreshLayout mTwinklingRefreshLayout;
    @InjectView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private List<MainDto> mList;
    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {
        mTwinklingRefreshLayout.setHeaderView(new RefreshHeaderView(getActivity()));
        mTwinklingRefreshLayout.setHeaderHeight(80);
        mTwinklingRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        mTwinklingRefreshLayout.finishRefreshing();
//                    }
//                }, 2000);
            }

            @Override
            public void onPullDownReleasing(TwinklingRefreshLayout refreshLayout, float fraction) {
                super.onPullDownReleasing(refreshLayout, fraction);
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        MainAdapter adapter= new MainAdapter(mList);
        mRecyclerView.setAdapter(adapter);
        mTwinklingRefreshLayout.setAutoLoadMore(false);
        mTwinklingRefreshLayout.setEnableLoadmore(false);
        mTwinklingRefreshLayout.setEnableOverScroll(true);
        mTwinklingRefreshLayout.setWaveHeight(240);

    }

    @Override
    protected void onUserVisible() {
        super.onUserVisible();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.frag_main;
    }
}
