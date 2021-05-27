package com.example.samples.data.models.mockhue

import android.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "lights")
class Light (
    @PrimaryKey var id: String = UUID.randomUUID().toString(),
    var name: String,
    var color: Int = Color.HSVToColor(FloatArray(3) { 0f})
    )