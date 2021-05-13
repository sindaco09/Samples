package com.example.newandroidxcomponentsdemo.data.models.coffee

import com.example.newandroidxcomponentsdemo.data.models.coffee.Roast
import com.example.newandroidxcomponentsdemo.data.models.coffee.Size
import com.example.newandroidxcomponentsdemo.data.models.coffee.Temp

class Coffee(
    var size: Size = Size.MEDIUM,
    var strong: Boolean = false,
    var temp: Temp = Temp.HOT,
    var bean: Roast = Roast.LIGHT)