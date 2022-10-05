package com.indaco.samples.data.models.news

open class News(val title: String, val type: Type) {

    enum class Type {ALERT, LOCAL, BREAKING}

    class LocalNews(title: String, val body: String): News(title, Type.LOCAL)
    class BreakingNews(title: String): News(title, Type.BREAKING)
    class AlertNews(title: String): News(title, Type.ALERT)
}