package com.javen.smartcloud.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.javen.smartcloud.R;
import com.javen.smartcloud.entity.HttpJokeResult;
import com.javen.smartcloud.entity.JokeEntity;
import com.javen.smartcloud.http.HttpMethods;
import com.javen.smartcloud.http.JokeService;
import com.javen.smartcloud.subscribers.ProgressSubscriber;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class FragmentOne extends BaseFragment {
    @BindView(R.id.id_joke_tv)
    TextView textview;
    @BindView(R.id.id_joke_btn)
    Button jokeBtn;

    Subscriber<JokeEntity> subscriber;

    Subscriber<HttpJokeResult<JokeEntity.Data>> subscriber2;

    Subscriber<JokeEntity.Data> subscriber3;

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

//        getJokes1(options);
//        getJokes2(options);
//        getJokes3(options);
//        getJokes4(options);
//        getJokes5(options);
        getJokes6(options);
    }
    /**
     * Retrofit + RxJava 封装版
     * 3.添加HttpMethods
     * 4.服务器返回固定格式的JSON 封装返回对象
     * 5.添加异常处理 并将data数据转换为目标数据类型传递给subscriber
     * 6.请求之前添加对话框提示
     * @param options
     */
    private void getJokes6(Map<String, Object> options){

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
    /**
     * Retrofit + RxJava 封装版
     * 3.添加HttpMethods
     * 4.服务器返回固定格式的JSON 封装返回对象
     * 5.添加异常处理 并将data数据转换为目标数据类型传递给subscriber
     * @param options
     */
    private void getJokes5(Map<String, Object> options){
        subscriber3 = new Subscriber<JokeEntity.Data>() {

            @Override
            public void onCompleted() {
                Toast.makeText(mContext, "Get Top Joke Completed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {
                textview.setText(e.getMessage());
            }

            @Override
            public void onNext(JokeEntity.Data data) {
                List<JokeEntity.Data.Subject> subjects = data.getData();
                for (JokeEntity.Data.Subject subject : subjects) {
                    Logger.i(subject.toString());
                }
                textview.setText(subjects.get(0).toString());
            }
        };

        HttpMethods.getInstance().getJokesByHttpResultMap(subscriber3,options);
    }
    /**
     * Retrofit + RxJava 封装版
     * 3.添加HttpMethods
     * 4.服务器返回固定格式的JSON 封装返回对象
     * @param options
     */
    private void getJokes4(Map<String, Object> options){
        subscriber2 = new Subscriber<HttpJokeResult<JokeEntity.Data>>() {

            @Override
            public void onCompleted() {
                Toast.makeText(mContext, "Get Top Joke Completed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {
                textview.setText(e.getMessage());
            }

            @Override
            public void onNext(HttpJokeResult<JokeEntity.Data> dataHttpJokeResult) {
                Logger.i(dataHttpJokeResult.toString());
                if(dataHttpJokeResult.getError_code()==0) {
                    List<JokeEntity.Data.Subject> data = dataHttpJokeResult.getResult().getData();
                    for (JokeEntity.Data.Subject subject : data) {
                        Logger.i(subject.toString());
                    }
                    textview.setText(data.get(0).toString());
                }else {
                    textview.setText(dataHttpJokeResult.toString());
                }
            }
        };

        HttpMethods.getInstance().getJokesByHttpResult(subscriber2,options);
    }

    /**
     * Retrofit + RxJava 封装版
     * 3.添加HttpMethods
     * @param options
     */
    private void getJokes3(Map<String, Object> options){
        subscriber = new Subscriber<JokeEntity>() {

            @Override
            public void onCompleted() {
                Toast.makeText(mContext, "Get Top Joke Completed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {
                textview.setText(e.getMessage());
            }

            @Override
            public void onNext(JokeEntity jokeEntity) {
                Logger.i(jokeEntity.toString());
                if(jokeEntity.getError_code()==0) {
                    List<JokeEntity.Data.Subject> data = jokeEntity.getResult().getData();
                    for (JokeEntity.Data.Subject subject : data) {
                        Logger.i(subject.toString());
                    }
                    textview.setText(data.get(0).toString());
                }else {
                    textview.setText(jokeEntity.toString());
                }
            }
        };
        HttpMethods.getInstance().getJokes(subscriber,options);
    }

    /**
     * Retrofit + RxJava
     * @param options
     */
    private void getJokes2(Map<String, Object> options) {

        String baseUrl="http://japi.juhe.cn/joke/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        JokeService jokeService = retrofit.create(JokeService.class);


        jokeService.getJokesByRxJava(options)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<JokeEntity>() {
                    @Override
                    public void onCompleted() {
                        Toast.makeText(mContext, "Get Top Joke Completed", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        textview.setText(e.getMessage());
                    }

                    @Override
                    public void onNext(JokeEntity jokeEntity) {
                        Logger.i(jokeEntity.toString());
                        if(jokeEntity.getError_code()==0) {
                            List<JokeEntity.Data.Subject> data = jokeEntity.getResult().getData();
                            for (JokeEntity.Data.Subject subject : data) {
                                Logger.i(subject.toString());
                            }
                            textview.setText(data.get(0).toString());
                        }else {
                            textview.setText(jokeEntity.toString());
                        }
                    }
                });

    }

    /**
     * 原生的Retrofit请求
     */
    private void getJokes1(Map<String, Object> options) {

        String baseUrl="http://japi.juhe.cn/joke/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JokeService jokeService = retrofit.create(JokeService.class);

        Call<JokeEntity> call = jokeService.getJokes(options);
        call.enqueue(new Callback<JokeEntity>() {
            @Override
            public void onResponse(Call<JokeEntity> call, Response<JokeEntity> response) {
                JokeEntity jokeEntity = response.body();
                Logger.i(jokeEntity.toString());
                if(jokeEntity.getError_code()==0) {
                    List<JokeEntity.Data.Subject> data = jokeEntity.getResult().getData();
                    for (JokeEntity.Data.Subject subject : data) {
                        Logger.i(subject.toString());
                    }
                    textview.setText(data.get(0).toString());
                }
            }

            @Override
            public void onFailure(Call<JokeEntity> call, Throwable t) {
                textview.setText(t.getMessage());
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (subscriber!=null){
            subscriber.unsubscribe();
        }
        if (subscriber2!=null){
            subscriber2.unsubscribe();
        }
        if (subscriber3!=null){
            subscriber3.unsubscribe();
        }
    }
}
