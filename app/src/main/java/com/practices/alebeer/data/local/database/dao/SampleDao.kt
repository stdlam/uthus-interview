package com.practices.alebeer.data.local.database.dao

import androidx.room.*
import com.practices.alebeer.data.local.database.AppRoomTable
import com.practices.alebeer.data.local.database.entity.SampleEntity

@Dao
interface SampleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSamples(items: List<SampleEntity>)

    @Query("SELECT * FROM ${AppRoomTable.TABLE_SAMPLE}")
    fun getFavSamples(): List<SampleEntity>?

    @Query("DELETE FROM ${AppRoomTable.TABLE_SAMPLE} WHERE remoteId = :id")
    fun deleteSample(id: Int)

    @Update
    fun updateSample(sample: SampleEntity)
}