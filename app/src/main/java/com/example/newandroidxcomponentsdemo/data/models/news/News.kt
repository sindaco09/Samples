package com.example.newandroidxcomponentsdemo.data.models.news

class News(val title: String, val type: Type) {

    enum class Type {ALERT, LOCAL, BREAKING}
}