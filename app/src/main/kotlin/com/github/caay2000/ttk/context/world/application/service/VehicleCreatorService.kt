package com.github.caay2000.ttk.context.world.application.service

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.right
import com.github.caay2000.ttk.context.core.domain.VehicleId
import com.github.caay2000.ttk.context.core.domain.WorldId
import com.github.caay2000.ttk.context.world.application.repository.WorldRepository
import com.github.caay2000.ttk.context.world.domain.Vehicle
import com.github.caay2000.ttk.context.world.domain.World

class VehicleCreatorService(private val worldRepository: WorldRepository) {

    fun invoke(worldId: WorldId, vehicleId: VehicleId, vehicleType: String, status: String): Either<Throwable, Unit> =
        worldRepository.get(worldId).right()
            .flatMap { world -> world.addVehicle(Vehicle(vehicleId, vehicleType, null, status)).right() }
            .flatMap { world -> world.save().right() }
            .flatMap { Unit.right() }

    private fun World.save() = worldRepository.save(this)
}
