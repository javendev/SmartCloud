package com.javen.smartcloud.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.javen.smartcloud.R;
import com.javen.smartcloud.entity.JokeEntity;
import com.javen.smartcloud.http.HttpMethods;
import com.javen.smartcloud.subscribers.ProgressSubscriber;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.EmptyWrapper;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class FragmentOne extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private final static int FIRST_WHAT = 0x456;
    private final static int REFRESH_WHAT = 0x457;
    private final static int LOADMORE_WHAT = 0x458;
    private final static int LOADINGNOM_WHAT = 0x459;
    @BindView(R.id.id_recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.id_swipe_refresh_widget)
    SwipeRefreshLayout swipeRefreshLayout;

    CommonAdapter commonAdapter;

    List<JokeEntity.Data.Subject> list;
    @BindView(R.id.retry_btn)
    Button retryBtn;
    @BindView(R.id.id_common_error_rl)
    RelativeLayout commom_error_rl;

    private LoadMoreWrapper mLoadMoreWrapper;
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private EmptyWrapper mEmptyWrapper;
    private int page = 1;
    Random random;

    private int colors[] = new int[]{R.color.google_blue, R.color.google_red, R.color.google_green, R.color.google_yellow, R.color.gray, R.color.white};

    public FragmentOne() {
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_one, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initData() {
        if (list == null) {
            list = new ArrayList<JokeEntity.Data.Subject>();
        }
        if (random == null) {
            random = new Random();
        }
        getJokes(1, FIRST_WHAT);
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

        recyclerview.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
//        recyclerview.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        commonAdapter = new CommonAdapter<JokeEntity.Data.Subject>(mContext, R.layout.item_joke_content, list) {
            @Override
            protected void convert(ViewHolder holder, JokeEntity.Data.Subject subject, int position) {
                holder.setText(R.id.id_joke_content, subject.getContent());
                holder.setText(R.id.id_joke_updateTime, subject.getUpdatetime());
                int num = random.nextInt(colors.length);
                holder.setTextColor(R.id.id_joke_content, colors[num]);
            }
        };

        commonAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
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
                getJokes(page, LOADMORE_WHAT);
            }
        });

        //        recyclerview.setAdapter(commonAdapter);
        recyclerview.setAdapter(mLoadMoreWrapper);
//        if (list == null || list.size()<=0){
//            //无数据时
//            recyclerview.setAdapter(mEmptyWrapper);
//        }else {
//            //加载更多
//            recyclerview.setAdapter(mLoadMoreWrapper);
//        }

    }

    private void initEmptyView() {
        mEmptyWrapper = new EmptyWrapper(commonAdapter);
        mEmptyWrapper.setEmptyView(LayoutInflater.from(mContext).inflate(R.layout.empty_view, recyclerview, false));
    }

    private void initHeaderAndFooter() {
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(commonAdapter);
    }


    private void getJokes(int page, final int type) {
        long timeMillis = System.currentTimeMillis();
        String time = String.valueOf(timeMillis).substring(0, 10);
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("key", "bd19bfd8cb856884b5739b0267ec24c9");
        options.put("sort", "desc");
        options.put("page", page);
        options.put("pagesize", 20);
        options.put("time", time);

        HttpMethods.getInstance().getJokesByHttpResultMap(new ProgressSubscriber<JokeEntity.Data>(mActivity) {
            @Override
            public void onDoNext(JokeEntity.Data data) {
                List<JokeEntity.Data.Subject> subjects = data.getData();

                if (type == FIRST_WHAT) {
                    list.clear();
                    list.addAll(subjects);
                } else if (type == REFRESH_WHAT) {
                    setRefredhingFalse();
                    list.clear();
                    list.addAll(subjects);
                } else if (type == LOADMORE_WHAT) {
                    list.addAll(subjects);
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
        }, options);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onRefresh() {
        getJokes(1, REFRESH_WHAT);
    }


    @OnClick(R.id.retry_btn)
    public void onClick() {
        getJokes(1, FIRST_WHAT);
    }

    /**
     * 隐藏错误页面
     */
    private void hideCommonError(){
        if (commom_error_rl.getVisibility()==View.VISIBLE){
            commom_error_rl.setVisibility(View.GONE);
        }
    }
    /**
     * 显示错误页面
     */
    private void showCommonError(){
        if (commom_error_rl.getVisibility()==View.GONE){
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
}
