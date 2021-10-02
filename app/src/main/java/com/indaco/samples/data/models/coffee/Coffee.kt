package com.indaco.samples.data.models.coffee

class Coffee(
    var size: Size = Size.MEDIUM,
    var strong: Boolean = false,
    var temp: Temp = Temp.HOT,
    var bean: Roast = Roast.LIGHT)