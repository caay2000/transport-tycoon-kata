package com.github.caay2000.ttk.context.vehicle.domain.world

import com.github.caay2000.ttk.context.shared.domain.StopId
import com.github.caay2000.ttk.context.shared.domain.WorldId

data class World(val id: WorldId, val stops: Set<Stop>) {

    companion object {
        fun create(worldId: WorldId): World = World(worldId, emptySet())
    }

    fun addStop(stop: Stop): World = this.copy(stops = stops + stop)

    fun getStop(stopName: String) = stops.first { it.name == stopName }
    fun getStop(id: StopId) = stops.first { it.id == id }
}
