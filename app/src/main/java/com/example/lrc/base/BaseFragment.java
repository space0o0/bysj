package com.example.lrc.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lrc.annotation.ActivityFragmentInject;
import com.example.lrc.weight.YTFjrToast;


/**
 *
 * Created by space on 16/3/23.
 */
public abstract class BaseFragment<T extends BasePresenter> extends Fragment implements BaseView, View.OnClickListener {

    // 将代理类通用行为抽出来
    protected T mPresenter;

    protected View fragmentRootView;
    protected int mContentViewId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (null == fragmentRootView) {
            if (getClass().isAnnotationPresent(ActivityFragmentInject.class)) {
                ActivityFragmentInject annotation = getClass().getAnnotation(ActivityFragmentInject.class);
                mContentViewId = annotation.contentViewId();
            } else {
//                throw new RuntimeException(
//                        "Class must add annotations of ActivityFragmentInitParams.class");
            }
            fragmentRootView=inflater.inflate(mContentViewId,container,false);
            initView(fragmentRootView);
        }
        return fragmentRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPresenter!=null){
            mPresenter.onResume();
            mPresenter.setView(this);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ViewGroup parent= (ViewGroup) fragmentRootView.getParent();
        if (parent!=null){
            parent.removeView(fragmentRootView);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter!=null){
            mPresenter.onDestroy();
        }

//        RefWatcher refWatcher= application.getInstance().getRefWatcher(getActivity());
    }

    protected abstract void initView(View fragmentRootView);

    @Override
    public void toast(String msg) {
        YTFjrToast.show(getActivity(),msg);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public Context getcontext() {
        return getActivity();
    }
}
