package com.github.caay2000.ttk.domain

import com.github.caay2000.ttk.api.inbound.Cargo
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.UUID

@Serializable
sealed class Vehicle {
    @Transient
    abstract val initialStop: Stop

    @Transient
    val id: UUID = UUID.randomUUID()

    private var cargo: Cargo? = null
    fun isEmpty() = cargo == null

    @Serializable
    private var status = VehicleStatus.IDLE

    @Transient
    private var route: Route? = null

    private fun load() {
        val loadedCargo = this.initialStop.retrieveCargo()
        this.cargo = loadedCargo
    }

    fun unload() {
        this.route!!.destination.deliverCargo(this.cargo!!)
        this.cargo = null
    }

    fun start() {
        this.route = Route(this.initialStop, getRouteDestination())
        this.status = VehicleStatus.ON_ROUTE
    }

    private fun getRouteDestination(): Stop {
        return when {
            initialStop == stops[Location.FACTORY] && cargo!!.destination == Location.WAREHOUSE_A -> stops[Location.PORT]!!
            else -> stops[cargo!!.destination]!!
        }
    }

    fun move() {
        this.route!!.update()
    }

    fun stop() {
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
    ON_ROUTE,
    STOP,
    LOADING,
    UNLOADING
}

@Serializable
data class Boat(override val initialStop: Stop) : Vehicle()

@Serializable
data class Truck(override val initialStop: Stop) : Vehicle()
