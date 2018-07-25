package com.example.sunze.contactdemo.listener;

import android.view.View;

/**
 * 新建于 Administrator 在 2017/2/23 0023.
 */

public interface OnItemClickListener {
    /**
     * 条目的点击事件
     * @param view 点中的条目
     * @param position 点中条目的索引
     */
    void onItemClick(View view, int position);
}
