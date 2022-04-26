package com.github.caay2000.ttk.context.vehicle.vehicle.domain

import com.github.caay2000.ttk.context.shared.domain.Distance
import com.github.caay2000.ttk.context.shared.domain.StopId
import com.github.caay2000.ttk.context.shared.domain.VehicleId
import com.github.caay2000.ttk.context.shared.domain.VehicleType
import com.github.caay2000.ttk.context.shared.domain.WorldId
import com.github.caay2000.ttk.context.shared.event.VehicleLoadedEvent
import com.github.caay2000.ttk.context.shared.event.VehicleUnloadedEvent
import com.github.caay2000.ttk.context.vehicle.cargo.domain.Cargo
import com.github.caay2000.ttk.context.vehicle.stop.domain.Stop
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

    private var task: VehicleTask = VehicleTask.IdleTask

    val taskFinished: Boolean
        get() = task.isFinished()

    abstract val loadTime: Int

    abstract val initialStop: Stop

    var cargo: Cargo? = null

    val status: VehicleStatus
        get() = task.status

    fun stop() {
        this.task = VehicleTask.IdleTask
    }

    fun update(): Vehicle {
        this.task = this.task.update()
        return this
    }

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
                (task as VehicleTask.OnRouteTask).targetStopId.uuid,
                cargo!!.sourceId.uuid,
                cargo!!.targetId.uuid
            )
        )
        this.cargo = null
        return this
    }

    fun startRoute(distance: Distance, sourceStopId: StopId, targetStopId: StopId): Vehicle {
        this.task = VehicleTask.OnRouteTask(distance, sourceStopId, targetStopId)
        return this
    }

    fun returnRoute(): Vehicle {
        this.task = (this.task as VehicleTask.OnRouteTask).toReturnBackRoute()
        return this
    }
}

enum class VehicleStatus {
    IDLE, ON_ROUTE, LOADING, UNLOADING, RETURNING
}

data class Boat(
    val idWorld: WorldId,
    val vehicleId: VehicleId,
    override val initialStop: Stop
) : Vehicle(idWorld, vehicleId, VehicleType.BOAT) {
    override val loadTime: Int = 2
}

data class Truck(
    val idWorld: WorldId,
    val vehicleId: VehicleId,
    override val initialStop: Stop
) : Vehicle(idWorld, vehicleId, VehicleType.TRUCK) {
    override val loadTime: Int = 1
}
