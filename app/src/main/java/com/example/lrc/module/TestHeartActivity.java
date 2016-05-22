package com.example.lrc.module;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import com.example.lrc.annotation.ActivityFragmentInject;
import com.example.lrc.base.BaseActivity;
import com.example.lrc.common.ConstantSet;
import com.example.lrc.view.HeartChartFragmen;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;

@ActivityFragmentInject(contentViewId = R.layout.activity_test_heart,
        toolbarIndicator = R.drawable.ic_menu_back)
public class TestHeartActivity extends BaseActivity {


    @Bind(R.id.framelayout_chart)
    FrameLayout framelayoutChart;
    HeartChartFragmen heartChartFragmen;
    FragmentManager manager;
    FragmentTransaction transaction;

    private final int TEST_DELAY = 30;//测试时间，过该时间后跳转

    Timer timer;
    TimerTask timerTask;

    @Override
    protected void initView() {
        ButterKnife.bind(this);

        initHeartLineFragment();

        initTimer();

        setToolbarTitle("测试心率中...");
    }

    public void initHeartLineFragment() {
        heartChartFragmen = HeartChartFragmen.newInstances(1000, R.color.blue, 30, "心率", false, ConstantSet.HEARTTYPE_MOVEMENT);

        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        transaction.replace(R.id.framelayout_chart, heartChartFragmen);
        transaction.commit();
    }

    public void initTimer() {
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                Intent i = new Intent();
                i.setClass(TestHeartActivity.this, IntroductionActivity.class);
                i.putExtra(ConstantSet.INTENT_HEART_TYPE, ConstantSet.HEARTTYPE_AFFABLE);
                startActivity(i);

                toast("心率紧张，请放舒缓歌曲");
                finish();
            }
        };

        timer.schedule(timerTask, TEST_DELAY * 1000);//30秒后提醒
    }
}
