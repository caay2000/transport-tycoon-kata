package com.github.caay2000.ttk.domain

import com.github.caay2000.ttk.api.inbound.Cargo
import com.github.caay2000.ttk.domain.Location.FACTORY
import com.github.caay2000.ttk.domain.Location.PORT

sealed class Vehicle {

    abstract val initialStop: Stop

    private var cargo: Cargo? = null
    fun isEmpty() = cargo == null

    private var status = VehicleStatus.IDLE

    private var route: Route? = null

    private fun load() {
        val loadedCargo = this.initialStop.retrieveCargo()
        this.cargo = loadedCargo
    }

    private fun unload() {
        this.route!!.destination.deliverCargo(this.cargo!!)
        this.cargo = null
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

    fun update() {
        if (status == VehicleStatus.IDLE && initialStop.hasCargo()) {
            load()
            start()
        }
        if (status == VehicleStatus.ON_ROUTE) {
            when {
                route!!.isStoppedInDestination() -> {
                    unload()
                    move()
                }
                route!!.isFinished() -> {
                    stop()
                    update()
                }
                else -> move()
            }
        }
    }
}

enum class VehicleStatus {
    IDLE,
    ON_ROUTE
}

data class Boat(override val initialStop: Stop) : Vehicle()

data class Truck(override val initialStop: Stop) : Vehicle()
