package com.github.caay2000.ttk.application

import com.github.caay2000.ttk.api.inbound.Cargo
import com.github.caay2000.ttk.api.inbound.Result
import com.github.caay2000.ttk.api.inbound.TransportTycoonApi
import com.github.caay2000.ttk.domain.Location.FACTORY
import com.github.caay2000.ttk.domain.Location.PORT
import com.github.caay2000.ttk.domain.Stop
import com.github.caay2000.ttk.domain.VehicleType
import com.github.caay2000.ttk.domain.World

internal class Application(private val MAX_NUM_ITERATIONS: Int = 200) : TransportTycoonApi {

    private val world = World(stops = Stop.all())

    override fun execute(cargo: List<Cargo>): Result {

        world.createVehicle(VehicleType.TRUCK, FACTORY)
        world.createVehicle(VehicleType.TRUCK, FACTORY)
        world.createVehicle(VehicleType.BOAT, PORT)

        world.addCargo(cargo)

        while (world.time < MAX_NUM_ITERATIONS && world.isCompleted().not()) {
            world.update()
        }

        return Result(world.time, world.events)
    }
}
