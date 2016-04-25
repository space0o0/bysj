package com.example.lrc.base;

/**
 * Created by space on 16/3/29.
 */
public class BasePresenterImpl<T extends BaseView> implements BasePresenter {

    public T mView;

    public BasePresenterImpl(T mView) {
        this.mView = mView;
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {
        mView = null;
    }

    @Override
    public void setView(Object view) {
        mView = (T) view;
    }


    public T getmView() {
        return mView;
    }
}
