package com.javen.smartcloud.http;

import com.javen.smartcloud.entity.GankEntity;
import com.javen.smartcloud.entity.HttpGankResult;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * 请求示例： http://gank.io/api/data/数据类型/请求个数/第几页
 * 数据类型： 福利 | Android | iOS | 休息视频 | 拓展资源 | 前端 | all
 * 请求个数： 数字，大于0
 * 第几页：数字，大于0
 */
public interface GankService {
    @GET("{type}/{pageSize}/{page}")
    Observable<HttpGankResult<List<GankEntity>>> getData(@Path("type") String type,@Path("pageSize") int pageSize,@Path("page") int page);

}