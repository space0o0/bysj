package com.example.lrc.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.lrc.Application.Application;
import com.example.lrc.common.ConstantSet;
import com.example.lrc.entity.music;

import java.io.IOException;
import java.util.List;

/**
 * Created by space on 16/3/4.
 * <p/>
 * 播放音乐的服务
 * 创建一个mediaplayer，在服务中调用它的各种播放方法即可，在服务中创建可支持后台播放，如放在activity中，当前activity关闭则停止播放歌曲，而且activity会特别卡
 */
public class MusicPlayerService extends Service {
    private final static String TAG = "-----musicPlayerService-----";
    private MediaPlayer player = new MediaPlayer();
    private String path = "";//音乐文件的路径
    private String new_path = "";//传入的音乐路径，在此判断新路径是否和正在播放的路径一样，如果一样，则暂停，否则，播放新的音乐
    private boolean isPause;//当前播放状态是否暂停
    private Application application;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate musicPlayerService!!!");
        application = Application.getInstance();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new myBinder();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (player == null) {
            player = new MediaPlayer();
        }


//        if (player.isPlaying()) {
//            stop();
//        }

        new_path = intent.getStringExtra("url");
        int msg = intent.getIntExtra("MSG", 0);
        if (msg == ConstantSet.PLAYER_PLAY_MSG) {

            if (path.equals(new_path)) {//两个地址相同
                if (player.isPlaying()) {
                    pause();
                } else {//继续播放歌曲
                    player.start();
                    //// TODO: 16/4/2 发送播放歌曲的广播
                    sendMusicStart();
                }
            } else {
                path = new_path;
                play(0);
            }

        } else if (msg == ConstantSet.PLAYER_PAUSE_MSG) {
            pause();
        } else if (msg == ConstantSet.PLAYER_STOP_MSG) {
            stop();
        }

        return super.onStartCommand(intent, flags, startId);
    }


    /**
     * 播放音乐
     * <p/>
     * 当为pause状态
     *
     * @param position
     */
    public void play(int position) {
        try {
            player.reset();//把各项参数恢复到初始状态
            player.setDataSource(path);
            player.prepare();//开始缓冲
            player.setOnPreparedListener(new preparedListener(position));
            player.setOnCompletionListener(new onCompletionListener());
            isPause = false;
            // TODO: 16/4/2 发送播放歌曲的广播
            sendMusicStart();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void play(String path1){
        new_path=path1;
        if (new_path.equals(path)){
            player.start();
        }else{
            replay(new_path);
            path=new_path;
        }
        sendMusicStart();
    }

    /**
     * 从头播放
     * @param path
     */
    public void replay(String path){

        try {
            player.reset();//把各项参数恢复到初始状态
            player.setDataSource(path);
            player.prepare();//开始缓冲
            player.setOnPreparedListener(new preparedListener(0));
            player.setOnCompletionListener(new onCompletionListener());
            isPause = false;
            // TODO: 16/4/2 发送播放歌曲的广播
            sendMusicStart();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 停止播放歌曲
     */
    public void stop() {
        if (player != null) {
            player.stop();
            // TODO: 16/4/2 发送停止歌曲的广播
            sendMusicStop();
            try {
                //在调用stop后如果需要再次通过start进行播放,需要之前调用prepare函数
                player.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void pause() {
        boolean i = player.isPlaying();
        if (player != null && player.isPlaying()) {
            player.pause();
            // TODO: 16/4/2 发送停止歌曲的广播
            sendMusicStop();
            isPause = true;
        }
    }

    public void sendMusicStart() {
        Intent intent = new Intent();
        intent.setAction(ConstantSet.BROADCAST_MUSIC_ACTION);
        intent.putExtra(ConstantSet.BROADCAST_MUSIC_EXTRAKEY, ConstantSet.BROADCAST_START_MUSIC);
        this.sendBroadcast(intent);
    }

    public void sendMusicStop() {
        Intent intent = new Intent();
        intent.setAction(ConstantSet.BROADCAST_MUSIC_ACTION);
        intent.putExtra(ConstantSet.BROADCAST_MUSIC_EXTRAKEY, ConstantSet.BROADCAST_STOP_MUSIC);
        this.sendBroadcast(intent);
    }

    /**
     * 实现一个OnPrepareLister接口,当音乐准备好的时候开始播放
     */
    private final class preparedListener implements MediaPlayer.OnPreparedListener {
        private int position;

        preparedListener(int position) {
            this.position = position;
        }

        @Override
        public void onPrepared(MediaPlayer mp) {
            player.start();//开始播放音乐
            if (position > 0) {//音乐不是从头开始播放
                player.seekTo(position);
            }
        }
    }

    private class onCompletionListener implements MediaPlayer.OnCompletionListener {


        @Override
        public void onCompletion(MediaPlayer mp) {
            Toast.makeText(Application.getInstance(), "一首歌播放完毕", Toast.LENGTH_LONG).show();

            List<music> list = application.getList_music();
            //查找下一首歌曲
            for (int i = 0; i < list.size(); i++) {
                String url = list.get(i).getUrl();
                if (url.equals(path)) {
                    if (i != (list.size() - 1)) {//不是最后一首歌曲
                        path = list.get(i + 1).getUrl();

                    } else//最后一首歌曲，则从头开始播放
                    {
                        path = list.get(0).getUrl();
                    }
                    break;
                }
            }

            play(0);

        }
    }

    public class myBinder extends Binder {

        public MusicPlayerService getService() {
            return MusicPlayerService.this;
        }
    }

    public MediaPlayer getPlayer() {
        return player;
    }

    public void setPlayer(MediaPlayer player) {
        this.player = player;
    }
}
