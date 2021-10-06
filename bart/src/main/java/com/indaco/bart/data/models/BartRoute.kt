package com.indaco.bart.data.models

import java.lang.RuntimeException

enum class BartRoute(val direction: BartStation.Direction, val routeId: String, val abbr: String, val color: String) {
    ROUTE_20(BartStation.Direction.SOUTH, "ROUTE 20","COLS","BEIGE"),
    ROUTE_19(BartStation.Direction.NORTH, "ROUTE 19","OAKL","BEIGE"),
    ROUTE_13(BartStation.Direction.NORTH, "ROUTE 13","SFIA","PURPLE"),
    ROUTE_14(BartStation.Direction.SOUTH, "ROUTE 14","MLBR","PURPLE"),
    ROUTE_12(BartStation.Direction.NORTH, "ROUTE 12","DUBL","BLUE"),
    ROUTE_11(BartStation.Direction.SOUTH, "ROUTE 11","DALY","BLUE"),
    ROUTE_8(BartStation.Direction.NORTH, "ROUTE 8","RICH","RED"),
    ROUTE_7(BartStation.Direction.SOUTH, "ROUTE 7","MLBR","RED"),
    ROUTE_6(BartStation.Direction.NORTH, "ROUTE 6","BERY","GREEN"),
    ROUTE_5(BartStation.Direction.SOUTH, "ROUTE 5","DALY","GREEN"),
    ROUTE_4(BartStation.Direction.SOUTH, "ROUTE 4","BERY","ORANGE"),
    ROUTE_3(BartStation.Direction.NORTH, "ROUTE 3","RICH","ORANGE"),
    ROUTE_2(BartStation.Direction.NORTH, "ROUTE 2","ANTC","YELLOW"),
    ROUTE_1(BartStation.Direction.SOUTH, "ROUTE 1","SFIA","YELLOW");

    companion object {
        fun getDirectionByRouteId(routeId: String?): BartStation.Direction {
            if (routeId.isNullOrEmpty())
                throw RuntimeException("Invalid routeId")
            else {
                val route = getRouteById(routeId)
                if (route != null)
                    return route.direction
                else
                    throw RuntimeException("Invalid routeId")
            }
        }

        fun getDirectionByStationAbbr(stationAbbr: String?): BartStation.Direction {
            if (stationAbbr.isNullOrEmpty())
                throw RuntimeException("Invalid stationAbbr: $stationAbbr")
            else {
                val route = values().find { it.abbr == stationAbbr }
                if (route != null)
                    return route.direction
                else
                    throw RuntimeException("Invalid stationAbbr $stationAbbr")
            }
        }

        fun getRouteByColorAndDir(_color: String, _direction: String): BartRoute? {
            val direction = BartStation.Direction.getDirectionByString(_direction)
            return values().find { it.color.equals(_color,true) && it.direction == direction}
        }

        fun getRouteById(routeId: String) = values().find { it.routeId == routeId }

    }
}
