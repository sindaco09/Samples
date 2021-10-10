package com.indaco.coffee.data.models

enum class State(var displayName: String) {
    STAND_BY("Stand By"),
    CHECK_FORM("Check Form"),
    CHECK_BEANS("Adding Beans"),
    CHECK_WATER("Adding Water"),
    BREWING("Brewing"),
    COMPLETE("Complete"),
    ERROR("Error!");

    var status = "Status: $displayName"

    companion object {
        fun getError(state: State) =
            when(state) {
                STAND_BY -> "Turn Power On"
                CHECK_FORM -> "Check Form"
                CHECK_BEANS -> "Add Beans"
                CHECK_WATER -> "Add Watter"
                BREWING -> "Malfunction"
                COMPLETE -> "Complete malfunction"
                ERROR -> "Error"
        }
    }
}