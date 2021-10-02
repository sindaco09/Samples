package com.example.core.core.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.core.data.models.bart.BartStation
import com.example.core.data.models.bart.BartTrip
import com.example.core.data.models.goal.Goal
import com.example.core.data.models.mockhue.Light
import com.example.core.data.storage.dao.BartDao
import com.example.core.data.storage.dao.GoalDao
import com.example.core.data.storage.dao.HueDao
import com.example.core.data.storage.dao.UserDao
import com.example.core.data.models.user.Converters
import com.example.core.data.models.user.User

@Database(entities = [User::class, Light::class, Goal::class, BartStation::class, BartTrip::class], version = 5, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun provideUserDao(): UserDao
    abstract fun provideHueDao(): HueDao
    abstract fun provideGoalsDao(): GoalDao
    abstract fun provideBartDao(): BartDao
}