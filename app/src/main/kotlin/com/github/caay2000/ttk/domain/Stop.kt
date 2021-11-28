package com.github.caay2000.ttk.domain

import java.util.UUID

data class Stop(val location: Location) {
    val id = UUID.randomUUID()

    fun distanceTo(stop: Stop) = this.location.distanceTo(stop.location)
}
