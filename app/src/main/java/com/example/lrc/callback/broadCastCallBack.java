package com.example.lrc.callback;

/**
 * 服务发送广播后的回调接口
 * Created by space on 16/4/2.
 */
public interface broadCastCallBack {
    /**
     * 开始音乐通知
     */
    void stopMusic();

    /**
     * 停止音乐通知
     */

    void startMusic();
}
