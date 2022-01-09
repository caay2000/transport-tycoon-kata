package com.github.caay2000.ttk.domain

import java.util.concurrent.atomic.AtomicInteger

data class Route(val start: Stop, val destination: Stop) {

    private val time: AtomicInteger = AtomicInteger(0)

    private val distanceToDestination = start.distanceTo(destination)
    private val totalRouteDistance = distanceToDestination * 2

    fun update() {
        time.incrementAndGet()
    }

    fun isStoppedInDestination() = time.get() == distanceToDestination
    fun isFinished() = time.get() >= totalRouteDistance
}
