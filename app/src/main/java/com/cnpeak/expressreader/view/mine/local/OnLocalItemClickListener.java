package com.cnpeak.expressreader.view.mine.local;

/**
 * RecyclerView item点击事件抽象类接口
 */
public interface OnLocalItemClickListener {
    //item条目点击事件
    void onItemClick(Object t);

    //空视图的点击刷新操作
    void refresh();

    //当前item的点选状态发生变化
    void onCheckable(boolean checkable);

    //当前选择的条目发生改变
    void onCheckedChanged(int count);
}
