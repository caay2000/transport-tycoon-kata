package com.github.caay2000.ttk.context.vehicle.domain.vehicle

import com.github.caay2000.ttk.context.shared.domain.VehicleId
import com.github.caay2000.ttk.context.shared.domain.VehicleType
import com.github.caay2000.ttk.context.shared.domain.WorldId
import com.github.caay2000.ttk.context.vehicle.application.route.FindRouteQuery
import com.github.caay2000.ttk.context.vehicle.application.route.FindRouteQueryResponse
import com.github.caay2000.ttk.context.vehicle.domain.cargo.Cargo
import com.github.caay2000.ttk.context.vehicle.domain.world.Stop
import com.github.caay2000.ttk.lib.event.VehicleCreatedEvent
import com.github.caay2000.ttk.lib.eventbus.domain.Aggregate
import com.github.caay2000.ttk.lib.eventbus.query.QueryBus

sealed class Vehicle(
    val worldId: WorldId,
    val id: VehicleId,
    val type: VehicleType,
    private val queryBus: QueryBus
) : Aggregate() {

//    private val vehicleId = VehicleId()

    companion object {
        fun create(worldId: WorldId, id: VehicleId, type: VehicleType, stop: Stop, queryBus: QueryBus) =
            when (type) {
                VehicleType.TRUCK -> Truck(worldId, id, stop, queryBus)
                VehicleType.BOAT -> Boat(worldId, id, stop, queryBus)
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
//        val loadedCargo = this.initialStop.retrieveCargo()
//        this.cargo = loadedCargo
    }

    private fun unload() {
//        this.route!!.destination.deliverCargo(this.cargo!!)
        this.cargo = null
    }

    private fun start() {
        this.route = Route(this.initialStop, getRouteDestination())
        this.status = VehicleStatus.ON_ROUTE
    }

    private fun getRouteDestination(): Stop {
        val routeResult = queryBus.execute<FindRouteQuery, FindRouteQueryResponse>(FindRouteQuery(this.id.uuid))

        return this.initialStop
//        when (initialStop.name == "FACTORY") && cargo!!.targetId
//        stopRepository.findByName("FACTORY")
//            .flatMap { stop -> }
//
// //        val world = stopRepository.findByName().get(this.worldId).getOrElse { throw RuntimeException("WorldNotFound") }
//        return when {
//            initialStop == world.getStop("FACTORY") && cargo!!.targetStopName == "WAREHOUSE_A" -> world.getStop("PORT")
//            else -> world.getStop(cargo!!.targetStopName)
//        }
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

data class Boat(
    val idWorld: WorldId,
    val vehicleId: VehicleId,
    override val initialStop: Stop,
    private val queryBus: QueryBus
) :
    Vehicle(idWorld, vehicleId, VehicleType.BOAT, queryBus)

data class Truck(
    val idWorld: WorldId,
    val vehicleId: VehicleId,
    override val initialStop: Stop,
    private val queryBus: QueryBus
) :
    Vehicle(idWorld, vehicleId, VehicleType.TRUCK, queryBus)
