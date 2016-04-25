package com.example.lrc.module;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import com.example.lrc.annotation.ActivityFragmentInject;
import com.example.lrc.base.BaseActivity;
import com.example.lrc.common.ConstantSet;
import com.example.lrc.view.HeartChartFragmen;

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

    @Override
    protected void initView() {
        ButterKnife.bind(this);

        initHeartLineFragment();


    }

    public void initHeartLineFragment(){
        heartChartFragmen=HeartChartFragmen.newInstances(1000,R.color.blue,30,"心率",false, ConstantSet.HEARTTYPE_MOVEMENT);

        manager=getSupportFragmentManager();
        transaction=manager.beginTransaction();
        transaction.replace(R.id.framelayout_chart,heartChartFragmen);
        transaction.commit();
    }

}
