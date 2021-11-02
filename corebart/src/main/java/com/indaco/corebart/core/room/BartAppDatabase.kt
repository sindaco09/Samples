package com.indaco.corebart.core.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.indaco.corebart.dao.BartDao
import com.indaco.corebart.models.bart.BartStationDbo
import com.indaco.corebart.models.bart.BartTripDbo
import com.indaco.core.models.user.Converters

@Database(entities = [BartStationDbo::class, BartTripDbo::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class BartAppDatabase: RoomDatabase() {
    abstract fun provideBartDao(): BartDao
}