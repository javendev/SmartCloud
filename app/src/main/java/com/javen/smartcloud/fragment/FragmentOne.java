package com.javen.smartcloud.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.javen.smartcloud.R;
import com.javen.smartcloud.entity.JokeEntity;
import com.javen.smartcloud.http.HttpMethods;
import com.javen.smartcloud.subscribers.ProgressSubscriber;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class FragmentOne extends BaseFragment {
    @BindView(R.id.id_joke_tv)
    TextView textview;
    @BindView(R.id.id_joke_btn)
    Button jokeBtn;


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

    }


    @OnClick(R.id.id_joke_btn)
    public void onClick() {
        long timeMillis = System.currentTimeMillis();
        String time=String.valueOf(timeMillis).substring(0,10);
        Logger.e("当前时间戳:"+timeMillis+"  前10位:"+time);
        Toast.makeText(mContext, "点我", Toast.LENGTH_SHORT).show();
        Map<String,Object> options = new HashMap<String, Object>();
        options.put("key","bd19bfd8cb856884b5739b0267ec24c9");
        options.put("sort","desc");
        options.put("page",1);
        options.put("pagesize",2);
        options.put("time",time);

        getJokes(options);
    }

    private void getJokes(Map<String, Object> options){

        HttpMethods.getInstance().getJokesByHttpResultMap(new ProgressSubscriber<JokeEntity.Data>(mActivity) {
            @Override
            public void onDoNext(JokeEntity.Data data) {
                List<JokeEntity.Data.Subject> subjects = data.getData();
                for (JokeEntity.Data.Subject subject : subjects) {
                    Logger.i(subject.toString());
                }
                textview.setText(subjects.get(0).toString());
            }
        }, options);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
