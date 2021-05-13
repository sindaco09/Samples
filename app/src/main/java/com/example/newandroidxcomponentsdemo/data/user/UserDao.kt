package com.example.newandroidxcomponentsdemo.data.user

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.newandroidxcomponentsdemo.data.models.user.User

@Dao
interface UserDao {

    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<User>?

    @Query("SELECT * FROM users WHERE username = :name LIMIT 1")
    suspend fun getUser(name: String): User?

    @Insert(onConflict = REPLACE)
    suspend fun addUser(user: User)

    @Delete
    suspend fun delete(user: User)

}