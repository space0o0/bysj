package com.example.lrc.base;

import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.v4.BuildConfig;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.lrc.annotation.ActivityFragmentInject;
import com.example.lrc.module.R;
import com.example.lrc.weight.YTFjrToast;


/**
 * Created by space on 16/3/18.
 * activity 基类
 */
public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity implements BaseView, View.OnClickListener {

    private int contentViewId;//顶部布局id
    private int toolbarTitle;//toolbar标题
    private int toolbarIndicator;//toolbar左边的按钮图片
    private int menuId;//菜单

    public T presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * 获取子activity的初始设置
         */
        if (getClass().isAnnotationPresent(ActivityFragmentInject.class)) {

            ActivityFragmentInject annotation = getClass().getAnnotation(ActivityFragmentInject.class);

            contentViewId = annotation.contentViewId();
            toolbarTitle = annotation.toolbarTitle();
            toolbarIndicator = annotation.toolbarIndicator();
            menuId = annotation.menuId();

        } else {
            throw new RuntimeException("Class must add annotation of ActivityFragmentInject.class");
        }

        /**
         * 设置严苛模式的调试特效
         */
        if (BuildConfig.DEBUG) {
            /**
             * 线程策略 针对主线程进行磁盘和网络访问，获得警告
             */
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
        } else {
            /**
             * 虚拟机策略 检查内存泄露
             */
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
        }

        setContentView(contentViewId);

        /** -------------------------------------
         * 后续如果需要添加侧滑，在此处初始化
         * --------------------------------------
         */

        initToolbar();

        initView();

    }

    protected abstract void initView();

    /**
     * 初始化toolbar，包括设置title和indicator
     */
    Toolbar toolbar;
    public void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            if (toolbarTitle != -1) {
                setToolbarTitle(toolbarTitle);
            }
            if (toolbarIndicator != -1) {
                setToolbarIndicator(toolbarIndicator);
            } else {
                //todo 如果没有图片，则设置indicator不显示
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//                setToolbarIndicator(-1);
            }


        }
    }



    /**
     * 设置toolbar的title
     *
     * @param toolbarTitle
     */
    public void setToolbarTitle(int toolbarTitle) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(toolbarTitle);
        }
    }

    /**
     * 程序中直接设置toolbar
     *
     * @param title
     */
    public void setToolbarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    /**
     * 设置toolbar左边的按钮
     *
     * @param toolbarIndicator
     */
    public void setToolbarIndicator(int toolbarIndicator) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(toolbarIndicator);
        }
    }

    /**
     * 获取顶层布局
     *
     * @return
     */
    public View getDecorView() {
        return getWindow().getDecorView();
    }

    /**
     * 获取toolbar
     *
     * @return
     */
    public Toolbar getToolbar() {
        return toolbar;
    }

    /**
     * 显示snackbar
     *
     * @param msg
     */
    public void showSnackBar(String msg) {
        Snackbar.make(getDecorView(), msg, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 子activity实现baseview接口
     *
     * @param msg
     */
    @Override
    public void toast(String msg) {
        YTFjrToast.show(this, msg);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public Context getcontext() {
        return this;
    }

    //-------------------------------------------------------------------------------------------


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (menuId != -1) {
            getMenuInflater().inflate(menuId, menu);

        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {//返回
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (presenter!=null){
            presenter.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter!=null){
            presenter.onDestroy();
        }
    }
}
