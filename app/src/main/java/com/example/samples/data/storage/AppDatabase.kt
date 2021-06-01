package com.example.samples.data.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.samples.data.models.goal.Goal
import com.example.samples.data.models.mockhue.Light
import com.example.samples.data.storage.user.UserDao
import com.example.samples.data.models.user.Converters
import com.example.samples.data.models.user.User
import com.example.samples.data.storage.hue.HueDao
import com.example.samples.data.storage.tasks.GoalDao

@Database(entities = [User::class, Light::class, Goal::class], version = 4, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun provideUserDao(): UserDao
    abstract fun provideHueDao(): HueDao
    abstract fun provideGoalsDao(): GoalDao
}