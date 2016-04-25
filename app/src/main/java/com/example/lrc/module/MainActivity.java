package com.example.lrc.module;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.lrc.view.ChartFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.fab)
    FloatingActionButton fab;

    private MusicListFragment musicListFragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;


    public boolean isShowFrag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        init();


    }

    /**
     * 初始化
     */
    public void init() {
        //创建musiclistfragment
        musicListFragment = new MusicListFragment();

        fragmentManager = getSupportFragmentManager();

        ChartFragment chartFragment=new ChartFragment().newInstance();
        transaction=fragmentManager.beginTransaction();
        transaction.add(R.id.main_framelayout,chartFragment);
        transaction.commit();
    }


    @OnClick(R.id.fab)
    void OnClick() {
            transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_out);

        if (isShowFrag) {//fragment显示
            setFab_MainActivity();
            isShowFrag = false;
        } else {//fragment未显示，
            setFab_MusicListFragment();
            isShowFrag = true;
        }

        transaction.commit();

    }

    /**
     * 隐藏musiclistfragment后
     * 设置floatingactionbutton的点击和背景
     */
    public void setFab_MainActivity() {

        hideFragment();
    }

    /**
     * 设置floatingactionbutton在显示fragment时的点击事件和背景
     */
    public void setFab_MusicListFragment() {

        showFragment();
    }

    /**
     * 在mainactivity中点击floatingactionbutton，显示musiclistfragment界面,从底部弹出动画
     */
    public void showFragment() {

        transaction.add(R.id.main_framelayout, musicListFragment);


    }

    public void hideFragment() {
        transaction.remove(musicListFragment);
    }

    //--------------------------------------------设置右上角的menu菜单---------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
