package com.github.caay2000.ttk.context.vehicle.domain

import com.github.caay2000.ttk.context.core.domain.Aggregate
import com.github.caay2000.ttk.context.core.domain.VehicleId
import com.github.caay2000.ttk.context.vehicle.application.repository.WorldRepository
import com.github.caay2000.ttk.context.world.domain.Location

sealed class Vehicle(val id: VehicleId, val type: VehicleType, private val worldRepository: WorldRepository) : Aggregate() {

    private val vehicleId = VehicleId()

    companion object {
        fun create(id: VehicleId, type: VehicleType, stop: Stop, worldRepository: WorldRepository) =
            when (type) {
                VehicleType.TRUCK -> Truck(id, stop, worldRepository)
                VehicleType.BOAT -> Boat(id, stop, worldRepository)
            }
    }

    abstract val initialStop: Stop

    private var cargo: Cargo? = null
    fun isEmpty() = cargo == null

    private var status = VehicleStatus.IDLE

    private var route: Route? = null

    private fun load() {
        val loadedCargo = this.initialStop.retrieveCargo()
        this.cargo = loadedCargo
        this.pushEvent(
            DepartedEvent(
                vehicleId = this.vehicleId,
                type = this.type,
                location = Location.valueOf(this.initialStop.name),
                destination = Location.valueOf(this.cargo!!.targetStopName),
                cargo = this.cargo!!
            )
        )
    }

    private fun unload() {
        this.route!!.destination.deliverCargo(this.cargo!!)
        this.pushEvent(
            ArrivedEvent(
                vehicleId = this.vehicleId,
                type = type,
                location = Location.valueOf(this.route!!.destination.name),
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
            worldRepository.get(this.)
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
    IDLE, ON_ROUTE
}

data class Boat(val vehicleId: VehicleId, override val initialStop: Stop, private val repo:WorldRepository) : Vehicle(vehicleId, VehicleType.BOAT, repo)

data class Truck(val vehicleId: VehicleId, override val initialStop: Stop, private val repo:WorldRepository) : Vehicle(vehicleId, VehicleType.TRUCK, repo)
