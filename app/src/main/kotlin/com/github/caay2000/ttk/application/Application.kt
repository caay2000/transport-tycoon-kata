package com.github.caay2000.ttk.application

import com.github.caay2000.ttk.api.inbound.Cargo
import com.github.caay2000.ttk.api.inbound.TransportTycoonApi
import com.github.caay2000.ttk.domain.Boat
import com.github.caay2000.ttk.domain.Location.FACTORY
import com.github.caay2000.ttk.domain.Location.PORT
import com.github.caay2000.ttk.domain.Truck
import com.github.caay2000.ttk.domain.World
import com.github.caay2000.ttk.domain.stops

internal class Application(private val MAX_NUM_ITERATIONS: Int = 200) : TransportTycoonApi {

    private val world = World(
        stops = stops.values.toList(),
        vehicles = listOf(Truck(stops[FACTORY]!!), Truck(stops[FACTORY]!!), Boat(stops[PORT]!!))
    )

    override fun execute(cargo: List<Cargo>): Int {

        world.addCargo(cargo)

        while (world.time < MAX_NUM_ITERATIONS && world.isCompleted().not()) {
            world.update()
        }

        return world.time
    }
}
