package com.github.caay2000.ttk.domain

data class Route private constructor(val stops: List<Stop>) {
    constructor(startLocation: Location, vararg stops: Location) : this(listOf(Stop(startLocation)) + stops.map { Stop(it) })

    private val Stop.nextStop: Stop
        get() {
            val stopIndex = stops.indexOfFirst { it.id == this.id }
            return if (stopIndex < numStops - 1) stops[stopIndex + 1]
            else stops[stopIndex]
        }

    var numStops: Int = stops.size

    val totalRouteDistance: Distance = stops.fold(initial = 0) { total, stop ->
        total + stop.distanceTo(stop.nextStop)
    }

    fun nextStop(stop: Stop) = stop.nextStop
}
