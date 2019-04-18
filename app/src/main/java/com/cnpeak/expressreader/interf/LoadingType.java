package com.cnpeak.expressreader.interf;

/**
 * 当前的数据加载类型
 */
public interface LoadingType {
    /**
     * 数据刷新操作
     */
    int LOADING_REFRESH = 0;
    /**
     * 加载上一条数据
     */
    int LOADING_PRE = 1;
    /**
     * 加载下一条数据
     */
    int LOADING_NEXT = 2;
    /**
     * 数据加载错误
     */
    int LOADING_ERROR = 3;
    /**
     * 数据加载错误
     */
    int LOADING_CONTENT = 4;

}
