package com.yx.lifetools.ui.fragment;

import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.yx.baselibrary.utils.HttpUtil;
import com.yx.baselibrary.utils.TLogUtil;
import com.yx.lifetools.R;
import com.yx.lifetools.bean.CommunityInfo;
import com.yx.lifetools.ui.a.ParseXml;
import com.yx.lifetools.ui.widget.BottomPopUpDialog;
import com.yx.lifetools.ui.widget.GlideImageLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @ClassName CommunityNewFragment
 * @Description 社区首页
 * Created by yx on 2017-03-07.
 */

public class CommunityNewFragment extends BaseFragment {

    @InjectView(R.id.iv_comunity)
    ImageView mComunity;
    List<String> images = new ArrayList<>();
    List<CommunityInfo> communityInfos = new ArrayList<>();
    private BottomPopUpDialog bottomPopUpDialog;
    private ParseXml parseXml;

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {
        images.add("http://b.hiphotos.baidu.com/image/h%3D200/sign=9d3833093f292df588c3ab158c305ce2/d788d43f8794a4c274c8110d0bf41bd5ad6e3928.jpg");
        images.add("http://c.hiphotos.baidu.com/image/pic/item/6c224f4a20a446237bf654449d22720e0cf3d7dc.jpg");
        images.add("http://b.hiphotos.baidu.com/image/pic/item/42166d224f4a20a43721cde195529822720ed0dd.jpg");
        Banner banner = (Banner) getActivity().findViewById(R.id.banner);
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(images);
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
//        banner.setBannerTitles(titles);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(1500);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
        bottomPopUpDialog = getBottomPopUpDialog();
        bottomPopUpDialog.builder();
        String[] strings = getActivity().getResources().getStringArray(R.array.community_name);
        TypedArray ar = getActivity().getResources().obtainTypedArray(R.array.community_icon);
        int len = ar.length();
        int[] resIds = new int[len];
        for (int i = 0; i < len; i++)
            resIds[i] = ar.getResourceId(i, 0);

        ar.recycle();
        for (int i = 0; i < strings.length; i++) {
            CommunityInfo communityInfo = new CommunityInfo();
            communityInfo.setName(strings[i]);
            communityInfo.setIconResId(resIds[i]);
            communityInfos.add(communityInfo);
        }
        mComunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomPopUpDialog.addSheetItem(communityInfos, new BottomPopUpDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {

                    }
                }, 3);
                bottomPopUpDialog.show();
            }
        });

        getXmlData();

        parseXml = new ParseXml();
        parseXml.saveXml();
        parseXml.parseXml(getActivity());
        parseXml.saxPull(getActivity());
        String xml = parseXml.getXml();
//        TLogUtil.e("xml", "" + xml);


    }

    private void getXmlData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://192.168.0.103:8080/app.xml";
                HttpUtil.sendHttpRequest(url, new HttpUtil.HttpCallBackListener() {
                    @Override
                    public void onFinsh(String response) {
                        TLogUtil.e("onFinsh===", response);
                        parseXml.pullParseXML(response.replaceAll("[^\\x20-\\x7e]", ""));
                        parseXml.saxParseXml(response.replaceAll("[^\\x20-\\x7e]", ""));
                    }

                    @Override
                    public void onError(Exception e) {
                        TLogUtil.e("onError===", e.getMessage());
                    }
                });
                HttpUtil.sendOkHttpRequest(url, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        TLogUtil.e("onFailure===", e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            TLogUtil.e("onResponse===", response.body().toString());
                        }
                    }
                });
            }
        }).start();

    }

    @NonNull
    private BottomPopUpDialog getBottomPopUpDialog() {
        return new BottomPopUpDialog(getActivity());
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.frag_community_new;
    }
}
