package com.dreampany.scan.data.source.local;

import androidx.room.Dao;
import androidx.room.Query;

import com.dreampany.framework.data.source.room.dao.BaseDao;
import com.dreampany.scan.data.model.Scan;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by Hawladar Roman on 7/2/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Dao
public interface ScanDao extends BaseDao<Scan> {
    @Query("select count(*) from scan")
    int count();

    @Query("select * from scan")
    Flowable<List<Scan>> getScans();

    @Query("select * from scan where id = :id limit 1")
    Flowable<Scan> getScanById(long id);

    @Query("select * from scan where type = :type")
    Flowable<List<Scan>> getScansByType(String type);
}
