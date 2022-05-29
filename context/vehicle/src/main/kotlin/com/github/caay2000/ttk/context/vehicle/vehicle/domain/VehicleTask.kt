package com.github.caay2000.ttk.context.vehicle.vehicle.domain

import com.github.caay2000.ttk.context.shared.domain.Distance
import com.github.caay2000.ttk.context.shared.domain.StopId
import com.github.caay2000.ttk.context.vehicle.world.domain.Cargo
import kotlin.math.roundToInt

internal sealed class VehicleTask(val status: VehicleStatus) {
    abstract val duration: Int
    abstract val progress: Int

    fun isFinished() = duration <= progress
    abstract fun update(): VehicleTask

    object IdleTask : VehicleTask(VehicleStatus.IDLE) {
        override val duration: Int = 0
        override val progress: Int = -1

        override fun update(): VehicleTask = this
    }

    data class LoadCargoTask internal constructor(
        override val duration: Int,
        override val progress: Int,
        val cargo: Cargo,
        val targetStopId: StopId,
        val targetStopDistance: Distance
    ) : VehicleTask(VehicleStatus.LOADING) {
        constructor(duration: Int, cargo: Cargo, targetStopId: StopId, targetStopDistance: Distance) : this(duration, 0, cargo, targetStopId, targetStopDistance)

        fun toOnRouteTask(currentStopId: StopId, speed: Double): OnRouteTask = OnRouteTask((this.targetStopDistance / speed).roundToInt(), currentStopId, this.targetStopId)
        override fun update(): VehicleTask = this.copy(progress = progress + 1)
    }

    data class UnloadCargoTask internal constructor(override val duration: Int, override val progress: Int, val cargo: Cargo, val stopId: StopId, val distance: Distance) :
        VehicleTask(VehicleStatus.UNLOADING) {
        constructor(duration: Int, cargo: Cargo, stopId: StopId, distance: Distance) : this(duration, 0, cargo, stopId, distance)

        override fun update(): VehicleTask = this.copy(progress = progress + 1)
    }

    data class OnRouteTask internal constructor(override val duration: Int, override val progress: Int, val sourceStopId: StopId, val targetStopId: StopId) :
        VehicleTask(VehicleStatus.ON_ROUTE) {
        constructor(duration: Int, sourceStopId: StopId, targetStopId: StopId) : this(duration, 0, sourceStopId, targetStopId)

        //        fun toReturnBackRoute(): ReturnBackRouteTask = ReturnBackRouteTask(duration, 0, sourceStopId, targetStopId)
        override fun update(): VehicleTask = this.copy(progress = progress + 1)
    }

    data class ReturnBackRouteTask internal constructor(override val duration: Int, override val progress: Int) :
        VehicleTask(VehicleStatus.RETURNING) {
        constructor(duration: Int, speed: Double) : this((duration / speed).roundToInt(), 0)

        override fun update(): VehicleTask = this.copy(progress = progress + 1)
    }
}
