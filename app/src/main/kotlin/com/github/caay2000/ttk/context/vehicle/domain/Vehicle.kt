package com.github.caay2000.ttk.context.vehicle.domain

import arrow.core.getOrElse
import com.github.caay2000.ttk.context.core.domain.VehicleId
import com.github.caay2000.ttk.context.core.domain.WorldId
import com.github.caay2000.ttk.context.vehicle.application.repository.WorldRepository
import com.github.caay2000.ttk.context.world.domain.Location
import com.github.caay2000.ttk.lib.event.VehicleCreatedEvent
import com.github.caay2000.ttk.lib.eventbus.domain.Aggregate

sealed class Vehicle(val worldId: WorldId, val id: VehicleId, val type: VehicleType, private val worldRepository: WorldRepository) : Aggregate() {

    private val vehicleId = VehicleId()

    companion object {
        fun create(worldId: WorldId, id: VehicleId, type: VehicleType, stop: Stop, worldRepository: WorldRepository) =
            when (type) {
                VehicleType.TRUCK -> Truck(worldId, id, stop, worldRepository)
                VehicleType.BOAT -> Boat(worldId, id, stop, worldRepository)
            }.also {
                it.pushEvent(VehicleCreatedEvent(worldId.uuid, id.uuid, type.name, it.status.name))
            }
    }

    abstract val initialStop: Stop

    var cargo: Cargo? = null
    fun isEmpty() = cargo == null

    var status = VehicleStatus.IDLE

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
        val world = worldRepository.get(this.worldId).getOrElse { throw RuntimeException("WorldNotFound") }
        return when {
            initialStop == world.getStop("FACTORY") && cargo!!.targetStopName == "WAREHOUSE_A" -> world.getStop("PORT")
            else -> world.getStop(cargo!!.targetStopName)
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

data class Boat(val idWorld: WorldId, val vehicleId: VehicleId, override val initialStop: Stop, private val repo: WorldRepository) :
    Vehicle(idWorld, vehicleId, VehicleType.BOAT, repo)

data class Truck(val idWorld: WorldId, val vehicleId: VehicleId, override val initialStop: Stop, private val repo: WorldRepository) :
    Vehicle(idWorld, vehicleId, VehicleType.TRUCK, repo)
