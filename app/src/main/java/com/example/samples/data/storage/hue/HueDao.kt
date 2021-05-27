package com.example.samples.data.storage.hue

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.example.samples.data.models.mockhue.Light

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