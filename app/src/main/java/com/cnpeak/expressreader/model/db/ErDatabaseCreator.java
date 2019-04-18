package com.cnpeak.expressreader.model.db;

import androidx.room.Room;
import android.content.Context;

/**
 * ExpressReader客户端数据库管理对象提供者
 * 采用单例模式维护实例对象
 */
public class ErDatabaseCreator {

    //数据库实现类对象
    private static ErDatabase instance = null;

    /**
     * 提供数据库操作类访问对象
     *
     * @param context 上下文
     */
    public static synchronized ErDatabase create(Context context) {
        if (instance == null) {
            synchronized (ErDatabaseCreator.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context, ErDatabase.class,
                            SQLConstant.DATABASE_NAME).build();
                }
            }
        }
        return instance;
    }

}
