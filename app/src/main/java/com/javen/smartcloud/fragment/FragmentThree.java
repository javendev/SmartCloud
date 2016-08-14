package com.javen.smartcloud.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.javen.smartcloud.R;
import com.javen.smartcloud.common.CommonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FragmentThree extends BaseFragment {

    @BindView(R.id.id_news_tabs)
    PagerSlidingTabStrip myNewsTabs;
    @BindView(R.id.viewPager)
    ViewPager myViewPager;

    public FragmentThree() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_three, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initData() {

    }

    @Override
    protected void initEvent() {
        // 初始化ViewPager并且添加适配器
        myViewPager.setAdapter(new myPagerAdapter(getChildFragmentManager()));
        //向ViewPager绑定PagerSlidingTabStrip
        myNewsTabs.setViewPager(myViewPager);
        myNewsTabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        FragmentNews.dataType = CommonUtils.GANK_TYPE_ALL;
                        break; case 1:
                        FragmentNews.dataType = CommonUtils.GANK_TYPE_ANDROID;
                        break; case 2:
                        FragmentNews.dataType = CommonUtils.GANK_TYPE_IOS;
                        break; case 3:
                        FragmentNews.dataType = CommonUtils.GANK_TYPE_WEB;
                        break; case 4:
                        FragmentNews.dataType = CommonUtils.GANK_TYPE_GIRL;
                        break; case 5:
                        FragmentNews.dataType = CommonUtils.GANK_TYPE_RESOURCE;
                        break; case 6:
                        FragmentNews.dataType = CommonUtils.GANK_TYPE_VIDEO;
                        break;
                    default:
                        FragmentNews.dataType = CommonUtils.GANK_TYPE_ALL;
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void loadData() {

    }

    class myPagerAdapter extends FragmentPagerAdapter {
        String[] title = { "All","Android","iOS","前端","妹子","拓展资源","休息视频"};
        FragmentNews fragmentNews;

        public myPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    fragmentNews = new FragmentNews(CommonUtils.GANK_TYPE_ALL);
                    return fragmentNews;
                case 1:

                case 2:
                    fragmentNews = new FragmentNews(CommonUtils.GANK_TYPE_ANDROID);
                    return fragmentNews;
                case 3:
                    fragmentNews = new FragmentNews(CommonUtils.GANK_TYPE_IOS);
                    return fragmentNews;
                case 4:
                    fragmentNews = new FragmentNews(CommonUtils.GANK_TYPE_WEB);
                    return fragmentNews;
                case 5:
                    fragmentNews = new FragmentNews(CommonUtils.GANK_TYPE_GIRL);
                    return fragmentNews;
                case 6:
                    fragmentNews = new FragmentNews(CommonUtils.GANK_TYPE_RESOURCE);
                    return fragmentNews;
                case 7:
                    fragmentNews = new FragmentNews(CommonUtils.GANK_TYPE_VIDEO);
                    return fragmentNews;

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return title.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return title[position];
        }

    }
}
