package com.example.samples.util.common

import android.content.res.Resources
import kotlin.math.roundToInt

object Ext {
    fun Int.toPx(): Int {
        val screenDensity = Resources.getSystem().displayMetrics.density
        return (this * screenDensity).roundToInt()
    }
}