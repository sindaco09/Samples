package com.indaco.samples.data.storage.user

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.indaco.samples.data.models.user.UserDbo

@Dao
interface UserDao {

    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<UserDbo>?

    @Query("SELECT * FROM users WHERE username = :name LIMIT 1")
    suspend fun getUser(name: String): UserDbo?

    @Insert(onConflict = REPLACE)
    suspend fun addUser(userDbo: UserDbo)

    @Delete
    suspend fun delete(userDbo: UserDbo)

}