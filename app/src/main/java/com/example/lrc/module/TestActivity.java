package com.example.lrc.module;

import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.view.View;

import com.example.lrc.annotation.ActivityFragmentInject;
import com.example.lrc.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

@ActivityFragmentInject(contentViewId = R.layout.activity_test,
        toolbarIndicator = R.drawable.ic_menu_back)
public class TestActivity extends BaseActivity {


    @Bind(R.id.bt_startTest)
    AppCompatButton btStartTest;

    @Override
    protected void initView() {
        ButterKnife.bind(this);

        btStartTest.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_startTest:
                toast("start test");
                Intent i=new Intent(this,TestHeartActivity.class);
                startActivity(i);
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
