package com.github.caay2000.ttk.domain

import com.github.caay2000.ttk.api.inbound.ArriveEvent
import com.github.caay2000.ttk.api.inbound.Cargo
import com.github.caay2000.ttk.api.inbound.DepartEvent
import com.github.caay2000.ttk.api.inbound.Event
import com.github.caay2000.ttk.domain.Location.FACTORY
import com.github.caay2000.ttk.domain.Location.PORT
import java.util.UUID

sealed class Vehicle {

    abstract val initialStop: Stop

    private val id = UUID.randomUUID()

    private var cargo: Cargo? = null
    fun isEmpty() = cargo == null

    private var status = VehicleStatus.IDLE

    private var route: Route? = null

    private fun load(): Event {
        val loadedCargo = this.initialStop.retrieveCargo()
        this.cargo = loadedCargo
        return DepartEvent(
            vehicleId = this.id,
            type = this::class,
            location = this.initialStop.location,
            destination = this.cargo!!.destination,
            cargo = this.cargo!!
        )
    }

    private fun unload(): Event {
        this.route!!.destination.deliverCargo(this.cargo!!)
        return ArriveEvent(
            vehicleId = this.id,
            type = this::class,
            location = this.route!!.destination.location,
            cargo = this.cargo!!
        ).also { this.cargo = null }
    }

    private fun start() {
        this.route = Route(this.initialStop, getRouteDestination())
        this.status = VehicleStatus.ON_ROUTE
    }

    private fun getRouteDestination(): Stop {
        return when {
            initialStop == Stop.get(FACTORY) && cargo!!.destination == Location.WAREHOUSE_A -> Stop.get(PORT)
            else -> Stop.get(cargo!!.destination)
        }
    }

    private fun move() {
        this.route!!.update()
    }

    private fun stop() {
        this.status = VehicleStatus.IDLE
        this.route = null
    }

    fun update(): List<Event> {

        val events = mutableListOf<Event>()

        if (status == VehicleStatus.IDLE && initialStop.hasCargo()) {
            events.add(load())
            start()
        }
        if (status == VehicleStatus.ON_ROUTE) {
            when {
                route!!.isStoppedInDestination() -> {
                    events.add(unload())
                    move()
                }
                route!!.isFinished() -> {
                    stop()
                    update()
                }
                else -> move()
            }
        }
        return events.toList()
    }
}

enum class VehicleStatus {
    IDLE, ON_ROUTE
}

data class Boat(override val initialStop: Stop) : Vehicle()

data class Truck(override val initialStop: Stop) : Vehicle()
