package com.example.lrc.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.lrc.common.ConstantSet;

/**
 *
 * Created by space on 16/4/2.
 */
public abstract class musicBroadCast extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        String action=intent.getAction();

        if (action.equals(ConstantSet.BROADCAST_MUSIC_ACTION)){

            String value = intent.getStringExtra(ConstantSet.BROADCAST_MUSIC_EXTRAKEY);

            if (value.equals(ConstantSet.BROADCAST_START_MUSIC))//接收到开始音乐
            {
                startMusic();

            }else if (value.equals(ConstantSet.BROADCAST_STOP_MUSIC))//接受到结束音乐
            {
                stopMusic();

            }
        }
    }

    public abstract void startMusic();
    public abstract void stopMusic();
}
