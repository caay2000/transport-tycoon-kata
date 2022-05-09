package com.github.caay2000.ttk.context.vehicle.vehicle.domain

import com.github.caay2000.ttk.context.shared.domain.Progress
import com.github.caay2000.ttk.context.shared.domain.StopId

sealed class Position {

    data class PositionIdle(val stopId: StopId) : Position()
    data class PositionOnRoute(val sourceStopId: StopId, val targetStopId: StopId, val progress: Progress) : Position()

    fun idle(): PositionIdle = this as PositionIdle
    fun onRoute(): PositionOnRoute = this as PositionOnRoute
}
