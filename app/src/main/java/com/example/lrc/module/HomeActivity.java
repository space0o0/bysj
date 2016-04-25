package com.example.lrc.module;

import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.view.View;

import com.example.lrc.annotation.ActivityFragmentInject;
import com.example.lrc.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

@ActivityFragmentInject(toolbarTitle = R.string.homeActivity_title,
        contentViewId = R.layout.activity_home)
public class HomeActivity extends BaseActivity {

    @Bind(R.id.bt_select)
    AppCompatButton btSelect;
    @Bind(R.id.bt_test)
    AppCompatButton btTest;

    @Override
    protected void initView() {
        ButterKnife.bind(this);

        btSelect.setOnClickListener(this);
        btTest.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_select://自主选择
                Intent i=new Intent(this,SelectActivity.class);
                startActivity(i);
                break;

            case R.id.bt_test://用户测试
                Intent i1=new Intent(this,TestActivity.class);
                startActivity(i1);
                break;
        }
        super.onClick(v);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
