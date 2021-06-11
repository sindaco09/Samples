package com.example.samples.core.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.samples.data.models.bart.BartStation
import com.example.samples.data.models.bart.BartTrip
import com.example.samples.data.models.goal.Goal
import com.example.samples.data.models.mockhue.Light
import com.example.samples.data.storage.user.UserDao
import com.example.samples.data.models.user.Converters
import com.example.samples.data.models.user.User
import com.example.samples.data.storage.bart.BartDao
import com.example.samples.data.storage.hue.HueDao
import com.example.samples.data.storage.goal.GoalDao

@Database(entities = [User::class, Light::class, Goal::class, BartStation::class, BartTrip::class], version = 5, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun provideUserDao(): UserDao
    abstract fun provideHueDao(): HueDao
    abstract fun provideGoalsDao(): GoalDao
    abstract fun provideBartDao(): BartDao
}