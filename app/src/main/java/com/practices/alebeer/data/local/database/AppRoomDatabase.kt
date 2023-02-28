package com.practices.alebeer.data.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.practices.alebeer.core.base.BaseApplication
import com.practices.alebeer.data.local.database.dao.SampleDao
import com.practices.alebeer.data.local.database.entity.SampleEntity
import com.practices.alebeer.helper.constant.DATABASE_NAME

@Database(
    entities = [SampleEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppRoomDatabase : RoomDatabase() {
    abstract fun createSampleDao(): SampleDao
}

fun createDatabase(application: BaseApplication) = Room.databaseBuilder(
    application,
    AppRoomDatabase::class.java,
    DATABASE_NAME
).fallbackToDestructiveMigration().allowMainThreadQueries().build()

fun createSampleDao(db: AppRoomDatabase) = db.createSampleDao()