package com.cnpeak.expressreader.model.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.cnpeak.expressreader.model.bean.MagazineList;

import java.util.List;

import io.reactivex.annotations.NonNull;

/**
 * 新闻详情数据库操作类对象
 * see {@link MagazineList}
 */
@Dao
public interface MagazineListDao {

    /**
     * 插入新闻数据
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(MagazineList bean);

    /**
     * 更新当前数据
     */
    @Update(onConflict = OnConflictStrategy.REPLACE)
    int update(MagazineList bean);

    /**
     * 批量插入报纸封面列表
     *
     * @param magazineList 当前的杂志封面列表数组
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long[] insertAll(@NonNull List<MagazineList> magazineList);

    /**
     * 查询当前收藏的列表
     */
    @Query("SELECT * FROM MagazineList ORDER BY checkedTimestamp DESC , originalIndex DESC")
    List<MagazineList> queryByOrderIndex();

    /**
     * 删除所有
     */
    @Query("DELETE  FROM MagazineList")
    int deleteAll();

//    /**
//     * 查询所有
//     */
//    @Query("SELECT * FROM MagazineList")
//    List<MagazineList> queryAll();

}
