package com.github.caay2000.ttk.context.vehicle.cargo.application.unload

import arrow.core.Either
import arrow.core.flatMap
import com.github.caay2000.ttk.context.shared.domain.CargoId
import com.github.caay2000.ttk.context.shared.domain.StopId
import com.github.caay2000.ttk.context.shared.domain.WorldId
import com.github.caay2000.ttk.context.vehicle.cargo.domain.Cargo
import com.github.caay2000.ttk.context.vehicle.world.domain.World
import com.github.caay2000.ttk.context.vehicle.world.domain.WorldRepository

class UnloadCargoService(private val worldRepository: WorldRepository) {

    fun invoke(worldId: WorldId, stopId: StopId, cargoId: CargoId, sourceStopId: StopId, targetStopId: StopId): Either<Throwable, Unit> =
        findWorld(worldId)
            .map { world -> world.unloadCargo(stopId, Cargo.create(cargoId, sourceStopId, targetStopId)) }
            .flatMap { world -> world.save() }

    private fun findWorld(worldId: WorldId): Either<Throwable, World> =
        worldRepository.get(worldId).toEither { UnloadCargoServiceException.WorldNotFound(worldId) }

    private fun World.save() = worldRepository.save(this).map { }
}
