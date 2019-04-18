package com.cnpeak.expressreader.interf;

public interface OnItemClickListener<T> {

    //点击条目回传数据
    void onItemClick(T t);

    //空视图刷新操作
    void refresh();
}
