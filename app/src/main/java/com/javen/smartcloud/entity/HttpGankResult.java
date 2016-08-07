package com.javen.smartcloud.entity;
/*
Http服务返回一个固定格式的数据
{
 "error": 0,
 "results":  []
}
 */
public class HttpGankResult<T> {
    private boolean error;
    private T results;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public T getResults() {
        return results;
    }

    public void setResults(T results) {
        this.results = results;
    }
}