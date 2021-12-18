package com.github.caay2000.ttk.application

import com.github.caay2000.ttk.api.inbound.Cargo
import com.github.caay2000.ttk.api.inbound.TransportTycoonApi
import com.github.caay2000.ttk.domain.Boat
import com.github.caay2000.ttk.domain.Location.FACTORY
import com.github.caay2000.ttk.domain.Location.PORT
import com.github.caay2000.ttk.domain.Truck
import com.github.caay2000.ttk.domain.World
import com.github.caay2000.ttk.domain.stops
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal class Application : TransportTycoonApi {

    private val world = World(
        stops = stops.values.toList(),
        vehicles = listOf(Truck(stops[FACTORY]!!), Truck(stops[FACTORY]!!), Boat(stops[PORT]!!))
    )

    override fun execute(cargo: List<Cargo>): Int {

        world.addCargo(cargo)

        while (world.isCompleted().not()) {

            println(Json.encodeToString(world))
            world.update()

            if (world.time > 100) break
        }
        return world.time
    }
}
