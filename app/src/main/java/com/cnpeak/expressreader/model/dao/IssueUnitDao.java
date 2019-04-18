package com.cnpeak.expressreader.model.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.cnpeak.expressreader.model.bean.IssueUnit;
import com.cnpeak.expressreader.model.db.DbType;

import java.util.List;

/**
 * 杂志详情数据库操作类对象
 * see {@link IssueUnit}
 */
@Dao
public interface IssueUnitDao {

    /**
     * 查询指定主键NID的新闻数据是否被收藏
     */
    @Query("SELECT * FROM IssueUnit WHERE MnId = :MnId")
    List<IssueUnit> queryIssueByMnId(String MnId);

    /**
     * 插入新闻数据
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(IssueUnit bean);

    /**
     * 删除单个
     */
    @Query("DELETE FROM IssueUnit WHERE MnId = :MnId AND dbType = :dbType")
    int deleteIssueByMnId(String MnId, @DbType int dbType);

    /**
     * 删除所有收藏记录
     */
    @Query("DELETE FROM IssueUnit WHERE dbType == :dbType")
    int deleteIssueUnitByType(@DbType int dbType);

    /**
     * 清除所有
     */
    @Query("DELETE FROM IssueUnit")
    int deleteAll();

    /**
     * 查询所有阅读记录
     */
    @Query("SELECT * FROM IssueUnit WHERE dbType = :dbType ORDER BY createDate DESC")
    List<IssueUnit> queryIssueUnitByType(@DbType int dbType);

}
