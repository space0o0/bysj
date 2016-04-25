package com.example.lrc.callback;

import android.view.View;

/**
 * Created by space on 16/4/5.
 */
public interface OnItemClickListener {

    void onItemClick(View v, int position);
    void onItemLongClick(View v, int position);
}
