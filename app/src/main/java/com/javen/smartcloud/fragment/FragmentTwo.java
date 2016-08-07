package com.javen.smartcloud.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.javen.smartcloud.R;
import com.javen.smartcloud.entity.GankEntity;
import com.javen.smartcloud.http.HttpGankMethods;
import com.javen.smartcloud.main.ImageActivity;
import com.javen.smartcloud.subscribers.ProgressSubscriber;
import com.javen.smartcloud.widget.imageloader.Imageloader;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.EmptyWrapper;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class FragmentTwo extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private final static int FIRST_WHAT = 0x456;
    private final static int REFRESH_WHAT = 0x457;
    private final static int LOADMORE_WHAT = 0x458;

    @BindView(R.id.id_girl_recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.id_girl_swipe_refresh_widget)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.retry_btn)
    Button retryBtn;
    @BindView(R.id.id_common_error_rl)
    RelativeLayout commom_error_rl;

    CommonAdapter commonAdapter;
    ArrayList<GankEntity> girlList;

    private LoadMoreWrapper mLoadMoreWrapper;
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private EmptyWrapper mEmptyWrapper;
    private int page = 1;

    public FragmentTwo() {
    }


    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_two, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initData() {
        if (girlList == null) {
            girlList = new ArrayList<GankEntity>();
        }
        getGank(1, FIRST_WHAT);
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
        // 这句话是为了，第girlS次进入页面的时候显示加载进度条
        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));

        recyclerview.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        commonAdapter = new CommonAdapter<GankEntity>(mContext, R.layout.item_girl_content, girlList) {
            @Override
            protected void convert(ViewHolder holder, GankEntity gankEntity, int position) {
                ImageView imageView = holder.getView(R.id.id_girl_pic);
                Imageloader.getInstance(mContext).setImage(gankEntity.getUrl(), imageView, R.drawable.default_item_picture);
            }
        };

        commonAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(mContext, ImageActivity.class);
                intent.putExtra("type",1);
                intent.putParcelableArrayListExtra("images",girlList);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(mContext, ImageActivity.class);
                intent.putExtra("type",0);
                intent.putExtra("imageUrl",girlList.get(position).getUrl());
                startActivity(intent);

                return false;
            }
        });


        initHeaderAndFooter();
        initEmptyView();

        mLoadMoreWrapper = new LoadMoreWrapper(mHeaderAndFooterWrapper);
        mLoadMoreWrapper.setLoadMoreView(R.layout.default_loading);
        mLoadMoreWrapper.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page = page + 1;
                getGank(page, LOADMORE_WHAT);
            }
        });

        recyclerview.setAdapter(mLoadMoreWrapper);
    }


    private void initEmptyView() {
        mEmptyWrapper = new EmptyWrapper(commonAdapter);
        mEmptyWrapper.setEmptyView(LayoutInflater.from(mContext).inflate(R.layout.empty_view, recyclerview, false));
    }

    private void initHeaderAndFooter() {
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(commonAdapter);
    }

    private void getGank(int page, final int type) {
        HttpGankMethods.getInstance().getGnakMap(new ProgressSubscriber<List<GankEntity>>(mActivity) {
            @Override
            public void onDoNext(List<GankEntity> gankEntities) {

                if (type == FIRST_WHAT) {
                    girlList.clear();
                    girlList.addAll(gankEntities);
                } else if (type == REFRESH_WHAT) {
                    setRefredhingFalse();
                    girlList.clear();
                    girlList.addAll(gankEntities);
                } else if (type == LOADMORE_WHAT) {
                    girlList.addAll(gankEntities);
                }
                hideCommonError();
                recyclerview.getAdapter().notifyDataSetChanged();
            }

            @Override
            protected void onCustomError(Throwable e) {
                setRefredhingFalse();
                showCommonError();
                super.onCustomError(e);
            }
        }, "福利", 20, page);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onRefresh() {
        getGank(1, REFRESH_WHAT);
    }


    @OnClick(R.id.retry_btn)
    public void onClick() {
        getGank(1, FIRST_WHAT);
    }

    /**
     * 隐藏错误页面
     */
    private void hideCommonError() {
        if (commom_error_rl.getVisibility() == View.VISIBLE) {
            commom_error_rl.setVisibility(View.GONE);
        }
    }

    /**
     * 显示错误页面
     */
    private void showCommonError() {
        if (commom_error_rl.getVisibility() == View.GONE) {
            commom_error_rl.setVisibility(View.VISIBLE);
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}