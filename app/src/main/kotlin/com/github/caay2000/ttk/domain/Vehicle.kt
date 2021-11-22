package com.github.caay2000.ttk.domain

import java.util.*

sealed class Vehicle {
    abstract val route: Route

    val id: UUID = UUID.randomUUID()

    fun startRoute(destination: Location) {
        this.route
    }
}

data class Boat(override val route: Route) : Vehicle()
data class Truck(override val route: Route) : Vehicle()

data class Route(
    val destination: Location
)