package com.example.newandroidxcomponentsdemo.data.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.newandroidxcomponentsdemo.data.storage.user.UserDao
import com.example.newandroidxcomponentsdemo.data.models.user.Converters
import com.example.newandroidxcomponentsdemo.data.models.user.User

@Database(entities = [User::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun provideUserDao(): UserDao
}