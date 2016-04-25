package com.example.lrc.callback;

/**
 *
 * Created by space on 16/3/29.
 */
public interface RequestCallBack<T> {

    /**
     * 请求前调用
     */
    void beforeRequest();

    /**
     * 请求完成调用
     */
    void requestComplete();

    /**
     * 请求错误调用
     * @param msg 错误信息
     */
    void requestError(String msg);

    /**
     * 请求成功调用
     * @param data 数据
     */
    void requestSuccess(T data);
}
