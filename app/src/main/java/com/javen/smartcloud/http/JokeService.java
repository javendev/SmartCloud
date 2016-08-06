package com.javen.smartcloud.http;

import com.javen.smartcloud.entity.HttpJokeResult;
import com.javen.smartcloud.entity.JokeEntity;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * 请求示例：http://japi.juhe.cn/joke/content/list.from?
 * key=您申请的KEY&page=2&pagesize=10&sort=asc&time=1418745237
 */
public interface JokeService {
    //Retrofit原生请求
    @GET("content/list.from")
    Call<JokeEntity> getJokes(@QueryMap Map<String, Object> options);

    //Retrofit + RxJava
    @GET("content/list.from")
    Observable<JokeEntity> getJokesByRxJava(@QueryMap Map<String, Object> options);


    //Retrofit + RxJava + 服务器返回统一JSON进行封装 返回的对象
    @GET("content/list.from")
    Observable<HttpJokeResult<JokeEntity.Data>> getJokesByRxJavaHttpResult(@QueryMap Map<String, Object> options);

}