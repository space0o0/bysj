package com.example.lrc.module;

import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.view.View;

import com.example.lrc.annotation.ActivityFragmentInject;
import com.example.lrc.base.BaseActivity;
import com.example.lrc.common.ConstantSet;

import butterknife.Bind;
import butterknife.ButterKnife;

@ActivityFragmentInject(contentViewId = R.layout.activity_select,
        toolbarTitle = R.string.title_activity_select,
        toolbarIndicator = R.mipmap.ic_menu_back)
public class SelectActivity extends BaseActivity {

    @Bind(R.id.bt_affable)
    AppCompatButton btAffable;
    @Bind(R.id.bt_movement)
    AppCompatButton btMovement;


    @Override
    protected void initView() {
        ButterKnife.bind(this);

        btAffable.setOnClickListener(this);
        btMovement.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        Intent i=new Intent(this,IntroductionActivity.class);
        switch (v.getId()){
            case R.id.bt_affable:
                i.putExtra(ConstantSet.INTENT_HEART_TYPE,ConstantSet.HEARTTYPE_AFFABLE);
                break;

            case R.id.bt_movement:
                i.putExtra(ConstantSet.INTENT_HEART_TYPE,ConstantSet.HEARTTYPE_MOVEMENT);
                break;
        }
        startActivity(i);

        super.onClick(v);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }


}
