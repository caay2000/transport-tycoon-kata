package com.github.caay2000.ttk.context.vehicle.domain.vehicle

import com.github.caay2000.ttk.context.shared.domain.VehicleId
import com.github.caay2000.ttk.context.shared.domain.VehicleType
import com.github.caay2000.ttk.context.shared.domain.WorldId
import com.github.caay2000.ttk.context.shared.event.VehicleLoadedEvent
import com.github.caay2000.ttk.context.shared.event.VehicleUnloadedEvent
import com.github.caay2000.ttk.context.vehicle.domain.cargo.Cargo
import com.github.caay2000.ttk.context.vehicle.domain.world.Stop
import com.github.caay2000.ttk.lib.eventbus.domain.Aggregate

sealed class Vehicle(
    val worldId: WorldId,
    val id: VehicleId,
    val type: VehicleType
) : Aggregate() {

    companion object {
        fun create(worldId: WorldId, id: VehicleId, type: VehicleType, stop: Stop) =
            when (type) {
                VehicleType.TRUCK -> Truck(worldId, id, stop)
                VehicleType.BOAT -> Boat(worldId, id, stop)
            }
    }

    abstract val initialStop: Stop

    var cargo: Cargo? = null
    fun isEmpty() = cargo == null

    var status = VehicleStatus.IDLE

    var route: Route? = null

    fun move() {
        this.route!!.update()
    }

    fun stop() {
        this.status = VehicleStatus.IDLE
        this.route = null
    }

    // OK FUNCTIONS

    fun loadCargo(cargo: Cargo): Vehicle {
        this.cargo = cargo
        this.pushEvent(
            VehicleLoadedEvent(
                this.worldId.uuid,
                this.id.uuid,
                cargo.id.uuid,
                this.initialStop.id.uuid,
                cargo.sourceId.uuid,
                cargo.targetId.uuid
            )
        )
        return this
    }

    fun unloadCargo(): Vehicle {
        this.pushEvent(
            VehicleUnloadedEvent(
                this.worldId.uuid,
                this.id.uuid,
                cargo!!.id.uuid,
                route!!.targetId.uuid,
                cargo!!.sourceId.uuid,
                cargo!!.targetId.uuid
            )
        )
        this.cargo = null
        return this
    }

    fun startRoute(route: Route): Vehicle {
        this.route = route
        this.status = VehicleStatus.ON_ROUTE
        return this
    }
}

enum class VehicleStatus {
    IDLE, ON_ROUTE
}

data class Boat(
    val idWorld: WorldId,
    val vehicleId: VehicleId,
    override val initialStop: Stop
) : Vehicle(idWorld, vehicleId, VehicleType.BOAT)

data class Truck(
    val idWorld: WorldId,
    val vehicleId: VehicleId,
    override val initialStop: Stop
) : Vehicle(idWorld, vehicleId, VehicleType.TRUCK)
