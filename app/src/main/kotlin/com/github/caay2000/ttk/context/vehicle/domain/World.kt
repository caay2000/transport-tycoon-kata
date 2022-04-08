package com.github.caay2000.ttk.context.vehicle.domain

import com.github.caay2000.ttk.context.core.domain.WorldId

data class World(val id: WorldId, val stops: Set<Stop>) {

    fun addStop(stop: Stop): World = this.copy(stops = stops + stop)

}
