package com.github.caay2000.ttk.context.vehicle.cargo.application.produce

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.right
import com.github.caay2000.ttk.context.shared.domain.CargoId
import com.github.caay2000.ttk.context.shared.domain.StopId
import com.github.caay2000.ttk.context.shared.domain.WorldId
import com.github.caay2000.ttk.context.vehicle.cargo.domain.Cargo
import com.github.caay2000.ttk.context.vehicle.world.domain.World
import com.github.caay2000.ttk.context.vehicle.world.domain.WorldRepository

class ProduceCargoService(private val worldRepository: WorldRepository) {

    fun invoke(worldId: WorldId, stopId: StopId, cargoId: CargoId, targetStopId: StopId): Either<Throwable, Unit> =
        findWorld(worldId)
            .flatMap { world -> world.produceCargo(stopId, Cargo.create(cargoId, stopId, targetStopId)).right() }
            .flatMap { world -> world.save() }

    private fun findWorld(worldId: WorldId): Either<Throwable, World> =
        worldRepository.get(worldId).toEither { ProduceCargoServiceException.WorldNotFound(worldId) }

    private fun World.save() = worldRepository.save(this).map { }
}
