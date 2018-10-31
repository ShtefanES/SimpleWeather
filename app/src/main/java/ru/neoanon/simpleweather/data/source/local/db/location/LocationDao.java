package ru.neoanon.simpleweather.data.source.local.db.location;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by eshtefan on  20.09.2018.
 */

@Dao
public interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertLocation(RegionLocation regionLocation);

    @Query("SELECT * FROM region_location WHERE id=:id")
    RegionLocation loadLocation(long id);

    @Query("SELECT * FROM region_location WHERE name=:locationName")
    RegionLocation loadLocation(String locationName);

    @Query("DELETE FROM region_location")
    void clearAllLocations();

    @Query("DELETE FROM region_location WHERE id=:id")
    int deleteRegionById(long id);

    @Query("SELECT * FROM region_location ORDER BY id ASC")
    Flowable<List<RegionLocation>> loadAllLocations();
}
