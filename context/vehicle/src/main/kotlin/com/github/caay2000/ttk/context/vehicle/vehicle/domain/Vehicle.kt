package com.github.caay2000.ttk.context.vehicle.vehicle.domain

import com.github.caay2000.ttk.context.shared.domain.Distance
import com.github.caay2000.ttk.context.shared.domain.StopId
import com.github.caay2000.ttk.context.shared.domain.VehicleId
import com.github.caay2000.ttk.context.shared.domain.VehicleTypeEnum
import com.github.caay2000.ttk.context.shared.domain.WorldId
import com.github.caay2000.ttk.context.shared.event.VehicleLoadedEvent
import com.github.caay2000.ttk.context.shared.event.VehicleLoadingEvent
import com.github.caay2000.ttk.context.shared.event.VehicleUnloadedEvent
import com.github.caay2000.ttk.context.shared.event.VehicleUnloadingEvent
import com.github.caay2000.ttk.context.vehicle.configuration.domain.VehicleConfiguration
import com.github.caay2000.ttk.context.vehicle.world.domain.Cargo
import com.github.caay2000.ttk.lib.eventbus.domain.Aggregate

data class Vehicle(
    val worldId: WorldId,
    val id: VehicleId,
    val type: VehicleType,
    val task: VehicleTask
) : Aggregate() {


    companion object {
        fun create(configuration: VehicleConfiguration, worldId: WorldId, id: VehicleId, type: VehicleTypeEnum, stopId: StopId) =
            Vehicle(worldId, id, VehicleType(type, configuration.loadTime, configuration.speed), VehicleTask.IdleTask(stopId))
    }

    val taskFinished: Boolean
        get() = task.isFinished()

    var cargo: Cargo? = null

    val status: VehicleStatus
        get() = task.status

//    fun stop() :Vehicle {
//        this.task = VehicleTask.IdleTask
//    }

    fun update(): Vehicle = this.copy(task = this.task.update())
//        this.task =
//        return this
//    }

    fun loadCargo(
        cargo: Cargo,
        routeTargetStopId: StopId,
        routeTargetStopDistance: Distance
    ): Vehicle = this.copy(task = VehicleTask.LoadCargoTask(this.type.loadTime, cargo, this.task.idle() routeTargetStopId, routeTargetStopDistance))
            .also {
                this.pushEvent(
                    VehicleLoadingEvent(
                        this.worldId.uuid,
                        this.id.uuid,
                        cargo.id.uuid,
                        this.position.idle().stopId.uuid
                    )
            }

    fun finishLoadingCargo(): Vehicle {
        this.cargo = (task as VehicleTask.LoadCargoTask).cargo
        this.pushEvent(
            VehicleLoadedEvent(
                this.worldId.uuid,
                this.id.uuid,
                this.cargo!!.id.uuid,
                this.position.idle().stopId.uuid,
                this.cargo!!.sourceId.uuid,
                this.cargo!!.targetId.uuid
            )
        )
        return this
    }

    fun unloadCargo(): Vehicle {
        this.task = VehicleTask.UnloadCargoTask(this.loadTime, this.cargo!!, (task as VehicleTask.OnRouteTask).targetStopId, (task as VehicleTask.OnRouteTask).duration)
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
            this.task = (this.task as VehicleTask.LoadCargoTask).toOnRouteTask(this.position.idle().stopId, this.speed)
        }
        return this
    }

    fun returnRoute(): Vehicle {
        val currentTask = (this.task as VehicleTask.UnloadCargoTask)
//        val distance = this.initialStop.distanceTo(currentTask.stopId)
        this.task = VehicleTask.ReturnBackRouteTask(currentTask.distance, this.speed)
        return this
    }
}

enum class VehicleStatus {
    IDLE, ON_ROUTE, LOADING, UNLOADING, RETURNING
}

//data class Boat(
//    val configuration: VehicleConfiguration,
//    val idWorld: WorldId,
//    val vehicleId: VehicleId,
//    val stopId: StopId
//) : Vehicle(idWorld, vehicleId, VehicleType.BOAT, VehicleTask.IdleTask(stopId)) {
//    override val loadTime: Int = configuration.loadTime
//    override val speed: Double = configuration.speed
//}

//data class Truck(
//    val configuration: VehicleConfiguration,
//    val idWorld: WorldId,
//    val vehicleId: VehicleId,
//    val stopId: StopId
//) : Vehicle(idWorld, vehicleId, VehicleType.TRUCK, VehicleTask.IdleTask(stopId)) {
//    override val loadTime: Int = configuration.loadTime
//    override val speed: Double = configuration.speed
//}
