package com.indaco.samples.core.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.indaco.samples.data.models.bart.BartStationDbo
import com.indaco.samples.data.models.bart.BartTripDbo
import com.indaco.samples.data.models.goal.Goal
import com.indaco.samples.data.models.mockhue.Light
import com.indaco.samples.data.storage.user.UserDao
import com.indaco.samples.data.models.user.Converters
import com.indaco.samples.data.models.user.User
import com.indaco.samples.data.storage.bart.BartDao
import com.indaco.samples.data.storage.hue.HueDao
import com.indaco.samples.data.storage.goal.GoalDao

@Database(entities = [User::class, Light::class, Goal::class, BartStationDbo::class, BartTripDbo::class], version = 5, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun provideUserDao(): UserDao
    abstract fun provideHueDao(): HueDao
    abstract fun provideGoalsDao(): GoalDao
    abstract fun provideBartDao(): BartDao
}