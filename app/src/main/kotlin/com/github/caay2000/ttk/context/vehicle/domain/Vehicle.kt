package com.github.caay2000.ttk.context.vehicle.domain

import com.github.caay2000.ttk.context.core.domain.Aggregate
import com.github.caay2000.ttk.context.core.domain.VehicleId
import com.github.caay2000.ttk.context.time.domain.DateTime
import com.github.caay2000.ttk.context.world.domain.Cargo
import com.github.caay2000.ttk.context.world.domain.Location
import com.github.caay2000.ttk.context.world.domain.Location.FACTORY
import com.github.caay2000.ttk.context.world.domain.Location.PORT
import com.github.caay2000.ttk.context.world.domain.Stop
import com.github.caay2000.ttk.context.world.domain.World

sealed class Vehicle(val type: VehicleType) : Aggregate() {

    private val vehicleId = VehicleId()

    companion object {
        fun create(world: World, type: VehicleType, startingLocation: Location) =
            when (type) {
                VehicleType.TRUCK -> Truck(world, world.getStop(startingLocation))
                VehicleType.BOAT -> Boat(world, world.getStop(startingLocation))
            }
    }

    abstract val world: World

    abstract val initialStop: Stop

    private var cargo: Cargo? = null
    fun isEmpty() = cargo == null

    private var status = VehicleStatus.IDLE

    private var route: Route? = null

    private fun load(dateTime: DateTime) {
        val loadedCargo = this.initialStop.retrieveCargo()
        this.cargo = loadedCargo
        this.pushEvent(
            DepartedEvent(
                time = dateTime,
                vehicleId = this.vehicleId,
                type = this.type,
                location = this.initialStop.location,
                destination = this.cargo!!.destination,
                cargo = this.cargo!!
            )
        )
    }

    private fun unload(dateTime: DateTime) {
        this.route!!.destination.deliverCargo(this.cargo!!)
        this.pushEvent(
            ArrivedEvent(
                time = dateTime,
                vehicleId = this.vehicleId,
                type = type,
                location = this.route!!.destination.location,
                cargo = this.cargo!!
            )
        )
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

    fun update(dateTime: DateTime) {

        if (status == VehicleStatus.IDLE && initialStop.hasCargo()) {
            load(dateTime)
            start()
        }
        if (status == VehicleStatus.ON_ROUTE) {
            when {
                route!!.isStoppedInDestination() -> {
                    unload(dateTime)
                    move()
                }
                route!!.isFinished() -> {
                    stop()
                    update(dateTime)
                }
                else -> move()
            }
        }
    }
}

enum class VehicleStatus {
    IDLE, ON_ROUTE
}

data class Boat(override val world: World, override val initialStop: Stop) : Vehicle(VehicleType.BOAT)

data class Truck(override val world: World, override val initialStop: Stop) : Vehicle(VehicleType.TRUCK)
