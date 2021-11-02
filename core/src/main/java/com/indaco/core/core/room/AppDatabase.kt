package com.indaco.core.core.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.indaco.core.dao.GoalDao
import com.indaco.core.dao.HueDao
import com.indaco.core.dao.UserDao
import com.indaco.core.models.mockhue.Light
import com.indaco.core.models.user.Converters
import com.indaco.core.models.user.User
import com.indaco.samples.data.models.goal.Goal

@Database(entities = [User::class, Light::class, Goal::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun provideUserDao(): UserDao
    abstract fun provideHueDao(): HueDao
    abstract fun provideGoalsDao(): GoalDao
}