package com.cnpeak.expressreader.model.db;

/**
 * 数据库常量定于类
 */
public class SQLConstant {
    /**
     * 数据库名称
     */
    static final String DATABASE_NAME = "expressreader_database";
    /**
     * 数据库版本号
     */
    static final int DATABASE_VERSION = 1;

    /**
     * 数据存储类型
     */
    public static final int DB_TYPE_HISTORY = 0;//历史记录
    public static final int DB_TYPE_FAVORITE = 1;//收藏记录
    static final int DB_TYPE_PAPER_COVER = 2;//PaperCover报纸封面数据类型
    static final int DB_TYPE_MAGAZINE_COVER = 3;//MagazineList杂志封面数据类型

}
