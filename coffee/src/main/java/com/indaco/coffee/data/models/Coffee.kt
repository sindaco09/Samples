package com.indaco.coffee.data.models

class Coffee(
    var size: Size = Size.MEDIUM,
    var strong: Boolean = false,
    var temperature: Temperature = Temperature.HOT,
    var bean: Roast = Roast.LIGHT)