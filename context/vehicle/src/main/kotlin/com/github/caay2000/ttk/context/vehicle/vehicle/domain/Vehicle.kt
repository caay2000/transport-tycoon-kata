package com.github.caay2000.ttk.context.vehicle.vehicle.domain

import com.github.caay2000.ttk.context.shared.domain.Distance
import com.github.caay2000.ttk.context.shared.domain.StopId
import com.github.caay2000.ttk.context.shared.domain.VehicleId
import com.github.caay2000.ttk.context.shared.domain.VehicleType
import com.github.caay2000.ttk.context.shared.domain.WorldId
import com.github.caay2000.ttk.context.shared.event.VehicleLoadedEvent
import com.github.caay2000.ttk.context.shared.event.VehicleLoadingEvent
import com.github.caay2000.ttk.context.shared.event.VehicleUnloadedEvent
import com.github.caay2000.ttk.context.shared.event.VehicleUnloadingEvent
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
    abstract val speed: Double

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

    fun loadCargo(
        cargo: Cargo,
        routeTargetStopId: StopId,
        routeTargetStopDistance: Distance
    ): Vehicle {
        this.task = VehicleTask.LoadCargoTask(this.loadTime, cargo, routeTargetStopId, routeTargetStopDistance)
        this.pushEvent(
            VehicleLoadingEvent(
                this.worldId.uuid,
                this.id.uuid,
                cargo.id.uuid,
                this.initialStop.id.uuid
            )

        )
        return this
    }

    fun finishLoadingCargo(): Vehicle {
        this.cargo = (task as VehicleTask.LoadCargoTask).cargo
        this.pushEvent(
            VehicleLoadedEvent(
                this.worldId.uuid,
                this.id.uuid,
                this.cargo!!.id.uuid,
                this.initialStop.id.uuid,
                this.cargo!!.sourceId.uuid,
                this.cargo!!.targetId.uuid
            )
        )
        return this
    }

    fun unloadCargo(): Vehicle {
        this.task = VehicleTask.UnloadCargoTask(this.loadTime, this.cargo!!, (task as VehicleTask.OnRouteTask).targetStopId)
        this.pushEvent(
            VehicleUnloadingEvent(
                this.worldId.uuid,
                this.id.uuid,
                cargo!!.id.uuid,
                (task as VehicleTask.UnloadCargoTask).stopId.uuid
            )
        )
        return this
    }

    fun finishUnloadingCargo(): Vehicle {
        this.cargo = null
        val currentTask = this.task as VehicleTask.UnloadCargoTask
        this.pushEvent(
            VehicleUnloadedEvent(
                this.worldId.uuid,
                this.id.uuid,
                currentTask.cargo.id.uuid,
                currentTask.stopId.uuid,
                currentTask.cargo.sourceId.uuid,
                currentTask.cargo.targetId.uuid
            )
        )

        return this
    }

    fun startRoute(): Vehicle {
        if (this.task is VehicleTask.LoadCargoTask) {
            this.task = (this.task as VehicleTask.LoadCargoTask).toOnRouteTask(this.initialStop.id, this.speed)
        }
        return this
    }

    fun returnRoute(): Vehicle {
        val currentTask = (this.task as VehicleTask.UnloadCargoTask)
        val distance = this.initialStop.distanceTo(currentTask.stopId)
        this.task = VehicleTask.ReturnBackRouteTask(distance, this.speed)
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
    override val speed: Double = 0.666666
}

data class Truck(
    val idWorld: WorldId,
    val vehicleId: VehicleId,
    override val initialStop: Stop
) : Vehicle(idWorld, vehicleId, VehicleType.TRUCK) {
    override val loadTime: Int = 1
    override val speed: Double = 1.0
}
