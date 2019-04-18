package com.cnpeak.expressreader.model.db;

import androidx.annotation.IntDef;

/**
 * 插入数据类型注解
 * 0：阅读历史
 * 1：收藏类型
 */
@IntDef({SQLConstant.DB_TYPE_HISTORY,
        SQLConstant.DB_TYPE_FAVORITE,
        SQLConstant.DB_TYPE_PAPER_COVER,
        SQLConstant.DB_TYPE_MAGAZINE_COVER})
public @interface DbType {
}
