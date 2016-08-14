package com.javen.smartcloud.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.javen.smartcloud.Adapter.NewsAdapter;
import com.javen.smartcloud.R;
import com.javen.smartcloud.common.CommonUtils;
import com.javen.smartcloud.common.DividerItemDecoration;
import com.javen.smartcloud.entity.GankEntity;
import com.javen.smartcloud.http.HttpGankMethods;
import com.javen.smartcloud.subscribers.ProgressSubscriber;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.wrapper.EmptyWrapper;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class FragmentNews extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{
    private final static int FIRST_WHAT = 0x456;
    private final static int REFRESH_WHAT = 0x457;
    private final static int LOADMORE_WHAT = 0x458;


    @BindView(R.id.retry_btn)
    Button retryBtn;
    @BindView(R.id.id_common_error_rl)
    RelativeLayout errorRelativeLayout;
    @BindView(R.id.id_news_recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.id_news_swipe_refresh_widget)
    SwipeRefreshLayout swipeRefreshLayout;

    //加载的数据类型
    public  static int dataType;

    private LoadMoreWrapper mLoadMoreWrapper;
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private EmptyWrapper mEmptyWrapper;
    private NewsAdapter mAdapter;

    private List<GankEntity> mDatas;

    private int page = 1;


    public FragmentNews(int dataType) {
        this.dataType = dataType;
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initData() {
        if (mDatas == null){
            mDatas = new ArrayList<>();
        }
    }

    @Override
    protected void initEvent() {
        swipeRefreshLayout.setColorSchemeResources(
                R.color.google_blue,
                R.color.google_green,
                R.color.google_red,
                R.color.google_yellow
        );
        swipeRefreshLayout.setOnRefreshListener(this);
        // 这句话是为了，第一次进入页面的时候显示加载进度条
        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        //设置布局样式
//        recyclerview.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity().getApplication()));
        //添加分割线
        recyclerview.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        //初始化Adapter
        mAdapter = new NewsAdapter(mContext,mDatas);
        initHeaderAndFooter();
        initEmptyView();

        mLoadMoreWrapper = new LoadMoreWrapper(mHeaderAndFooterWrapper);
        mLoadMoreWrapper = new LoadMoreWrapper(mHeaderAndFooterWrapper);
        mLoadMoreWrapper.setLoadMoreView(R.layout.default_loading);
        mLoadMoreWrapper.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page = page + 1;
                getGank(page, LOADMORE_WHAT,dataType);
            }
        });

        recyclerview.setAdapter(mLoadMoreWrapper);
        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Toast.makeText(mContext, "onClick position:" + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                Toast.makeText(mContext, "OnLongClick position:" + position, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    @Override
    public void loadData() {
        getGank(1, FIRST_WHAT,dataType);
    }


    private void getGank(int page, final int loadType,int dataType) {
        String type="all";
        if (dataType == CommonUtils.GANK_TYPE_ALL){
            type="all";
        }else if (dataType == CommonUtils.GANK_TYPE_ANDROID){
            type="Android";
        }else if (dataType == CommonUtils.GANK_TYPE_IOS){
            type="iOS";
        }else if (dataType == CommonUtils.GANK_TYPE_GIRL){
            type="福利";
        }else if (dataType == CommonUtils.GANK_TYPE_VIDEO){
            type="休息视频";
        }else if (dataType == CommonUtils.GANK_TYPE_WEB){
            type="前端";
        }else if (dataType == CommonUtils.GANK_TYPE_RESOURCE){
            type="拓展资源";
        }


        HttpGankMethods.getInstance().getGnakMap(new ProgressSubscriber<List<GankEntity>>(mActivity) {
            @Override
            public void onDoNext(List<GankEntity> gankEntities) {
                if (loadType == FIRST_WHAT) {
                    mDatas.clear();
                    mDatas.addAll(gankEntities);
                } else if (loadType == REFRESH_WHAT) {
                    setRefredhingFalse();
                    mDatas.clear();
                    mDatas.addAll(gankEntities);
                } else if (loadType == LOADMORE_WHAT) {
                    mDatas.addAll(gankEntities);
                }
                hideCommonError();
                recyclerview.getAdapter().notifyDataSetChanged();
            }

            @Override
            protected void onCustomError(Throwable e) {
                setRefredhingFalse();
                if(loadType == FIRST_WHAT){
                    showCommonError();
                }
                super.onCustomError(e);
            }
        }, type, 20, page);
    }

    @Override
    public void onRefresh() {
        getGank(1, FIRST_WHAT,dataType);
    }

    private void initEmptyView() {
        mEmptyWrapper = new EmptyWrapper(mAdapter);
        mEmptyWrapper.setEmptyView(LayoutInflater.from(mContext).inflate(R.layout.empty_view, recyclerview, false));
    }

    private void initHeaderAndFooter() {
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(mAdapter);
    }
    @OnClick(R.id.retry_btn)
    public void onClick() {
        getGank(1, FIRST_WHAT,dataType);
    }

    /**
     * 隐藏错误页面
     */
    private void hideCommonError() {
        if (errorRelativeLayout.getVisibility() == View.VISIBLE) {
            errorRelativeLayout.setVisibility(View.GONE);
        }
    }

    /**
     * 显示错误页面
     */
    private void showCommonError() {
        if (errorRelativeLayout.getVisibility() == View.GONE) {
            errorRelativeLayout.setVisibility(View.VISIBLE);
        }
    }

    /**
     * swipeRefreshLayout刷新提示设置我false
     */
    private void setRefredhingFalse() {
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}