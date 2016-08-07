package com.javen.smartcloud.http;

import com.javen.smartcloud.entity.GankEntity;
import com.javen.smartcloud.entity.HttpGankResult;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class HttpGankMethods {

    public static final String BASE_URL = "http://gank.io/api/data/";

    private static final int DEFAULT_TIMEOUT = 5;

    private Retrofit retrofit;

    private GankService gankService;


    //构造方法私有
    private HttpGankMethods() {
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).build();



        retrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        gankService = retrofit.create(GankService.class);
    }

    //在访问HttpMethods时创建单例
    private static class SingletonHolder{
        private static final HttpGankMethods INSTANCE = new HttpGankMethods();
    }

    //获取单例
    public static HttpGankMethods getInstance(){
        return SingletonHolder.INSTANCE;
    }


    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    private class HttpResultFunc<T> implements Func1<HttpGankResult<T>, T> {

        @Override
        public T call(HttpGankResult<T> httpResult) {
            if (httpResult.isError()) {
                throw new ApiException(001);
            }
            return httpResult.getResults();
        }
    }

    private <T> void toSubscribe(Observable<T> observable, Subscriber<T> subscriber){
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 用于获取聚合笑话的数据
     * @param subscriber 由调用者传过来的观察者对象
     */
    public void getGnakMap(Subscriber<List<GankEntity>> subscriber, String type, int pageSize, int page){
        Observable<List<GankEntity>> observable = gankService.getData(type,pageSize,page)
                .map(new HttpResultFunc<List<GankEntity>>());
        toSubscribe(observable,subscriber);
    }
}
