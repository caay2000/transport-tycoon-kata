package com.github.caay2000.ttk.domain

import com.github.caay2000.ttk.domain.event.Arrived
import com.github.caay2000.ttk.domain.event.Departed
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

    fun update(currentTime: Int): List<VehicleEvent> {
        return when (status) {
            VehicleStatus.IDLE -> idleHandler(currentTime)
            VehicleStatus.ON_ROUTE -> onRouteHandler(currentTime)
            else -> emptyList()
        }
    }

    private fun idleHandler(currentTime: Int): List<VehicleEvent> {
        status = VehicleStatus.ON_ROUTE
        return listOf(Departed(currentTime, location, this))
    }

    private fun onRouteHandler(currentTime: Int): List<VehicleEvent> {
        _route!!.distanceTravelled += speed
        return when (distanceToNextStop) {
            0 -> {
                _route!!.distanceTravelled = 0
                _route!!.updateCurrentStop()
                location = _route!!.lastStop.location
                if (_route!!.isLastStop()) {
                    this.status = VehicleStatus.STOP
                    listOf(Arrived(currentTime, location, this))
                } else {
                    listOf(
                        Arrived(currentTime, location, this),
                        Departed(currentTime, location, this)
                    )
                }
            }
            else -> {
                emptyList()
            }
        }
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
