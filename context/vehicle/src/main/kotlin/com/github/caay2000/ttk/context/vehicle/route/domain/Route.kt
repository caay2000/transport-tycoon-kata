package com.github.caay2000.ttk.context.vehicle.route.domain

import com.github.caay2000.ttk.context.shared.domain.Distance
import com.github.caay2000.ttk.context.shared.domain.StopId
import java.util.concurrent.atomic.AtomicInteger

data class Route(val sourceId: StopId, val targetId: StopId, val distance: Distance) {

    private val time: AtomicInteger = AtomicInteger(0)
    private val totalRouteDistance = distance * 2

    fun update() {
        time.incrementAndGet()
    }

    fun isStoppedInDestination() = time.get() == distance
    fun isFinished() = time.get() >= totalRouteDistance
}
