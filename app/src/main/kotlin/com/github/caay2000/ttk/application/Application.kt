package com.github.caay2000.ttk.application

import com.github.caay2000.ttk.api.inbound.Cargo
import com.github.caay2000.ttk.api.inbound.Result
import com.github.caay2000.ttk.api.inbound.TransportTycoonApi
import com.github.caay2000.ttk.domain.Boat
import com.github.caay2000.ttk.domain.Location.FACTORY
import com.github.caay2000.ttk.domain.Location.PORT
import com.github.caay2000.ttk.domain.Stop
import com.github.caay2000.ttk.domain.Truck
import com.github.caay2000.ttk.domain.World

internal class Application(private val MAX_NUM_ITERATIONS: Int = 200) : TransportTycoonApi {

    private val world = World(
        stops = Stop.all(),
        vehicles = listOf(Truck(Stop.get(FACTORY)), Truck(Stop.get(FACTORY)), Boat(Stop.get(PORT)))
    )

    override fun execute(cargo: List<Cargo>): Result {

        world.addCargo(cargo)

        while (world.time < MAX_NUM_ITERATIONS && world.isCompleted().not()) {
            world.update()
        }

        return Result(world.time, emptyList())
    }
}
