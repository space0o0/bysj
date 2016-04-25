package com.example.lrc.common;

/**
 * Created by space on 16/3/4.
 */
public class ConstantSet {

    public static final int PLAYER_PLAY_MSG=0;
    public static final int PLAYER_PAUSE_MSG=1;
    public static final int PLAYER_STOP_MSG=2;


    //广播的action
    public static final String BROADCAST_MUSIC_ACTION="BYSJ_BC";
    //广播发送的extra的key
    public static final String BROADCAST_MUSIC_EXTRAKEY="MUSIC_STATE";
    //接受广播的extra的value
    public static final String BROADCAST_STOP_MUSIC="MUSIC_STOP";
    public static final String BROADCAST_START_MUSIC="MUSIC_START";

    public static final int ITEM_VIEWTYPE_EMPTY=2001;
    public static final int ITEM_VIEWTYPE_NORMAL=2002;

    //心率类型
    public static final int HEARTTYPE_AFFABLE=1;//舒缓型
    public static final int HEARTTYPE_MOVEMENT=2;//运动型


    public static final String INTENT_HEART_TYPE="HEARTTYPE";
    public static final String INTENT_MUSIC_CURR="MUSICCURR";
}
