package com.example.lrc.module;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.lrc.Application.Application;
import com.example.lrc.annotation.ActivityFragmentInject;
import com.example.lrc.base.BaseActivity;
import com.example.lrc.common.ConstantSet;
import com.example.lrc.entity.music;
import com.example.lrc.service.MusicPlayerService;
import com.example.lrc.view.HeartChartFragmen;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

@ActivityFragmentInject(contentViewId = R.layout.activity_play_music,
        toolbarIndicator = R.drawable.ic_menu_back)
public class PlayMusicActivity extends BaseActivity {


    @Bind(R.id.LineChart)
    FrameLayout LineChart;
    @Bind(R.id.playMusic_previous)
    ImageView playMusicPrevious;
    @Bind(R.id.playMusic_play)
    ImageView playMusicPlay;
    @Bind(R.id.playMusic_next)
    ImageView playMusicNext;

    int currMusic = 0;
    int musicType = 1;
    List<music> musics;
    HeartChartFragmen fragment;
    FragmentManager manager;
    FragmentTransaction transaction;
    MusicPlayerService mservice;

    ConnectionService connectionService;

    @Override
    protected void initView() {
        ButterKnife.bind(this);

        connectionService = new ConnectionService();
        //绑定服务
        Intent intent = new Intent();
        intent.setAction("com.example.lrc.service.MusicPlayerService.MSG_ACTION");
        intent.setPackage(getPackageName());
        bindService(intent, connectionService, Context.BIND_AUTO_CREATE);

        currMusic = getIntent().getIntExtra(ConstantSet.INTENT_MUSIC_CURR, 0);
        musicType = getIntent().getIntExtra(ConstantSet.INTENT_HEART_TYPE, ConstantSet.HEARTTYPE_AFFABLE);
        if (musicType == ConstantSet.HEARTTYPE_AFFABLE) {
            musics = Application.getInstance().getMusics_affable();
        } else {
            musics = Application.getInstance().getMusics_movement();
        }

        initFragment();

        playMusicPrevious.setOnClickListener(this);
        playMusicNext.setOnClickListener(this);
        playMusicPlay.setOnClickListener(this);

    }

    public class ConnectionService implements ServiceConnection {

        /**
         * Called when a connection to the Service has been established, with
         * the {@link IBinder} of the communication channel to the
         * Service.
         *
         * @param name    The concrete component name of the service that has
         *                been connected.
         * @param service The IBinder of the Service's communication channel,
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mservice = ((MusicPlayerService.myBinder) service).getService();
        }

        /**
         * Called when a connection to the Service has been lost.  This typically
         * happens when the process hosting the service has crashed or been killed.
         * This does <em>not</em> remove the ServiceConnection itself -- this
         * binding to the service will remain active, and you will receive a call
         * to {@link #onServiceConnected} when the Service is next running.
         *
         * @param name The concrete component name of the service whose
         *             connection has been lost.
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    public void initFragment() {
        fragment = HeartChartFragmen.newInstances(1000, R.color.blue, 30, "心率", true, musicType);
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        transaction.replace(R.id.LineChart, fragment);
        transaction.commit();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.playMusic_previous://上一首
                toast("previous");

                break;
            case R.id.playMusic_next://下一首
                toast("next");

                break;
            case R.id.playMusic_play://播放或暂停
                toast("play");
                play();
                break;
        }
        super.onClick(v);
    }

    public void previous() {

    }

    public void next() {

    }


    public void play() {
        if (mservice.getPlayer().isPlaying()) {
            mservice.pause();
        } else {

            mservice.play(musics.get(currMusic).getUrl());
        }
    }

    @Override
    protected void onDestroy() {
        unbindService(connectionService);
        super.onDestroy();
    }
}
