package com.github.caay2000.ttk.context.vehicle.vehicle.domain

import com.github.caay2000.ttk.context.shared.domain.StopId

internal sealed class VehicleTask(val status: VehicleStatus) {
    abstract val duration: Int
    abstract val progress: Int

    fun isFinished() = duration <= progress
    abstract fun update(): VehicleTask

    object IdleTask : VehicleTask(VehicleStatus.IDLE) {
        override val duration: Int = 0
        override val progress: Int = 0

        override fun update(): VehicleTask = this
    }

    data class LoadCargoTask internal constructor(override val duration: Int, override val progress: Int) : VehicleTask(VehicleStatus.LOADING) {
        constructor(duration: Int) : this(duration, 0)

        override fun update(): VehicleTask = this.copy(progress = progress + 1)
    }

    data class UnloadCargoTask internal constructor(override val duration: Int, override val progress: Int) : VehicleTask(VehicleStatus.UNLOADING) {
        constructor(duration: Int) : this(duration, 0)

        override fun update(): VehicleTask = this.copy(progress = progress + 1)
    }

    data class OnRouteTask internal constructor(override val duration: Int, override val progress: Int, val sourceStopId: StopId, val targetStopId: StopId) :
        VehicleTask(VehicleStatus.ON_ROUTE) {
        constructor(duration: Int, sourceStopId: StopId, targetStopId: StopId) : this(duration, 0, sourceStopId, targetStopId)

        fun toReturnBackRoute(): ReturnBackRouteTask = ReturnBackRouteTask(duration, 0, sourceStopId, targetStopId)
        override fun update(): VehicleTask = this.copy(progress = progress + 1)
    }

    data class ReturnBackRouteTask internal constructor(override val duration: Int, override val progress: Int, val sourceStopId: StopId, val targetStopId: StopId) :
        VehicleTask(VehicleStatus.RETURNING) {
//        constructor(duration: Int, sourceStopId: StopId, targetStopId: StopId) : this(duration, 0, sourceStopId, targetStopId)

        override fun update(): VehicleTask = this.copy(progress = progress + 1)
    }
}
