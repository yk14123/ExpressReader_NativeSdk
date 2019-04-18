package com.cnpeak.expressreader.model.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.cnpeak.expressreader.model.bean.HotSpot;
import com.cnpeak.expressreader.model.bean.IssueUnit;
import com.cnpeak.expressreader.model.bean.MagazineList;
import com.cnpeak.expressreader.model.bean.PaperCover;
import com.cnpeak.expressreader.model.dao.HotSpotDao;
import com.cnpeak.expressreader.model.dao.IssueUnitDao;
import com.cnpeak.expressreader.model.dao.MagazineListDao;
import com.cnpeak.expressreader.model.dao.PaperCoverDao;

/**
 * The type Er database.
 */
@Database(entities = {HotSpot.class, IssueUnit.class, PaperCover.class, MagazineList.class},
        version = SQLConstant.DATABASE_VERSION,
        exportSchema = false)
@TypeConverters({Converters.class})
public abstract class ErDatabase extends RoomDatabase {
    /**
     * 新闻详情数据库操作类对象
     * bean see {@link HotSpot}
     *
     * @return the hot spot dao
     */
    public abstract HotSpotDao hotSpotDao();

    /**
     * 杂志详情数据库操作类对象
     * bean see{@link IssueUnit}
     *
     * @return the issue unit dao
     */
    public abstract IssueUnitDao issueUnitDao();

    /**
     * 报纸封面数据库操作对象
     * bean see {@link PaperCover}
     *
     * @return the paper cover dao
     */
    public abstract PaperCoverDao paperCoverDao();

    /**
     * 杂志封面数据库操作对象
     * bean see {@link MagazineList}
     *
     * @return the magazine list dao
     */
    public abstract MagazineListDao magazineListDao();

}
