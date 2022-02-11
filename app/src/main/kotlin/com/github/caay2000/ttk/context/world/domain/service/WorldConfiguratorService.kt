package com.github.caay2000.ttk.context.world.domain.service

import com.github.caay2000.ttk.context.core.domain.WorldId
import com.github.caay2000.ttk.context.vehicle.domain.VehicleType
import com.github.caay2000.ttk.context.world.domain.Cargo
import com.github.caay2000.ttk.context.world.domain.Location
import com.github.caay2000.ttk.context.world.domain.Stop
import com.github.caay2000.ttk.context.world.domain.World
import com.github.caay2000.ttk.context.world.domain.repository.WorldRepository

class WorldConfiguratorService(private val worldRepository: WorldRepository) {

    fun create(worldId: WorldId): World {

        val world = World(id = worldId, stops = Stop.all())

        world.createVehicle(VehicleType.TRUCK, Location.FACTORY)
        world.createVehicle(VehicleType.TRUCK, Location.FACTORY)
        world.createVehicle(VehicleType.BOAT, Location.PORT)

        return world.save()
    }

    fun addCargo(worldId: WorldId, cargo: Cargo): World {

        val world = worldRepository.get(worldId)
        world.addCargo(cargo)
        return world.save()
    }

    private fun World.save() = worldRepository.save(this)
}
