package com.github.caay2000.ttk.domain

import com.github.caay2000.ttk.domain.event.VehicleEvent
import java.util.UUID

sealed class Vehicle(private val speed: Int) {

    val id: UUID = UUID.randomUUID()

    abstract var location: Location
        internal set

    var status: VehicleStatus = VehicleStatus.STOP
        internal set

    private var _route: VehicleRoute? = null
    var route: Route? = null
        get() = _route?.route
        private set

    val distanceToNextStop: Int
        get() = _route?.distanceToNextStop ?: 0

    private inner class VehicleRoute(val route: Route) {
        fun shouldMove(): Boolean = listOf(VehicleStatus.IDLE, VehicleStatus.ON_ROUTE).contains(status)

        fun reachedStation(): Boolean = distanceToNextStop == 0
        fun updateCurrentStop() {
            if (currentStop < route.stops.size - 1) currentStop++
        }

        var currentStop: Int = 0
        val lastStop: Stop
            get() = route.stops[currentStop]
        val nextStop: Stop
            get() = route.nextStop(route.stops[currentStop])

        var distanceTravelled = 0
        val distanceToNextStop: Int
            get() = lastStop.distanceTo(nextStop) - distanceTravelled

        override fun toString(): String {
            return "VehicleRoute(route=$route, currentStop=$currentStop, lastStop=$lastStop, nextStop=$nextStop, distanceTravelled=$distanceTravelled, distanceToNextStop=$distanceToNextStop)"
        }

        fun isLastStop(): Boolean = currentStop == route.numStops - 1
    }

    fun assignRoute(route: Route) {
        this._route = VehicleRoute(route)
        this.status = VehicleStatus.IDLE
    }

    fun update(): List<VehicleEvent> {
        _route?.let {
            if (it.shouldMove()) {
                status = VehicleStatus.ON_ROUTE
                _route!!.distanceTravelled += speed
                if (it.reachedStation()) {
                    status = VehicleStatus.IDLE
                    _route!!.distanceTravelled = 0
                    it.updateCurrentStop()
                    this.location = it.lastStop.location
                    if (it.isLastStop()) {
                        this.status = VehicleStatus.STOP
                    }
                }
            }
        }
        return emptyList()
    }

    override fun toString(): String {
        return "Vehicle(speed=$speed, id=$id, location=$location, status=$status, _route=$_route, route=$route)"
    }
}

enum class VehicleStatus {
    IDLE,
    ON_ROUTE,
    STOP,
    LOADING,
    UNLOADING
}

data class Boat(override var location: Location) : Vehicle(1) {
    override fun toString(): String {
        return super.toString()
    }
}

data class Truck(override var location: Location) : Vehicle(1) {
    override fun toString(): String {
        return super.toString()
    }
}
