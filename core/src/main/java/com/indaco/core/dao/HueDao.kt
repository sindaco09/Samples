package com.indaco.core.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.indaco.core.models.mockhue.Light

@Dao
interface HueDao {

    @Insert(onConflict = REPLACE)
    fun addLight(light: Light)

    @Query("SELECT * FROM lights")
    fun getLights(): List<Light>?

    @Query("SELECT * FROM lights WHERE id == :uuid LIMIT 1 ")
    fun getLight(uuid: String): Light?

    @Update
    fun updateLight(light: Light)
}