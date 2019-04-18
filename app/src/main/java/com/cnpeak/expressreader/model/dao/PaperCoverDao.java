package com.cnpeak.expressreader.model.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.cnpeak.expressreader.model.bean.PaperCover;

import java.util.List;

/**
 * 新闻详情数据库操作类对象
 * see {@link PaperCover}
 */
@Dao
public interface PaperCoverDao {
//    /**
//     * 查询指定NewID主键的列表项数据
//     */
//    @Query("SELECT * FROM PaperCover WHERE NewsID == :NewsID")
//    List<PaperCover> queryPaperByNewsId(String NewsID);

    /**
     * 插入报纸项数据
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(PaperCover bean);

    /**
     * 更新当前数据
     */
    @Update(onConflict = OnConflictStrategy.REPLACE)
    int update(PaperCover bean);

    /**
     * 批量插入报纸封面列表
     *
     * @param paperCovers 当前的报纸列表数组
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long[] insertAll(List<PaperCover> paperCovers);

    /**
     * 查询当前收藏的列表
     */
    @Query("SELECT * FROM PaperCover ORDER BY checkedTimestamp DESC , originalIndex DESC")
    List<PaperCover> queryByOrderIndex();

    /**
     * 删除所有
     */
    @Query("DELETE FROM PaperCover")
    int deleteAll();

//    /**
//     * 查询所有
//     */
//    @Query("SELECT * FROM PaperCover")
//    List<PaperCover> queryAll();

}
