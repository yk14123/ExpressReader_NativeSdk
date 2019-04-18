package com.cnpeak.expressreader.model.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.cnpeak.expressreader.model.bean.HotSpot;
import com.cnpeak.expressreader.model.db.DbType;

import java.util.List;

/**
 * 新闻详情数据库操作类对象
 * see {@link HotSpot}
 */
@Dao
public interface HotSpotDao {
    /**
     * 查询指定NID的新闻数据在数据库存储的类型状态
     *
     * @param NID the nid
     * @return the list
     */
    @Query("SELECT * FROM HotSpot WHERE NID == :NID")
    List<HotSpot> queryNewsByNID(String NID);

    /**
     * 插入新闻数据
     *
     * @param bean the bean
     * @return the rowId[]
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(HotSpot bean);

    /**
     * 删除所有指定的数据类型
     *
     * @param dbType the db type
     * @return the rowId
     */
    @Query("DELETE FROM HotSpot WHERE dbType = :dbType")
    int deleteNewsByType(@DbType int dbType);

    /**
     * 删除指定项
     *
     * @param NID    the nid
     * @param dbType the db type
     * @return the rowId
     */
    @Query("DELETE FROM HotSpot WHERE NID = :NID AND dbType = :dbType")
    int deleteNewsByNID(String NID, @DbType int dbType);

    /**
     * 清除所有
     *
     * @return the rowId
     */
    @Query("DELETE FROM HotSpot")
    int deleteAll();

    /**
     * 查询所有收藏
     *
     * @param dbType the db type
     * @return the list
     */
    @Query("SELECT * FROM HotSpot WHERE dbType = :dbType ORDER BY createDate DESC")
    List<HotSpot> queryNewsByType(@DbType int dbType);

}
