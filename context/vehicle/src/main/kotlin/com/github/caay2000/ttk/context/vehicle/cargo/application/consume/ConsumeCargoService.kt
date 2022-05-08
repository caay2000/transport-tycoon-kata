package com.github.caay2000.ttk.context.vehicle.cargo.application.consume

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import com.github.caay2000.ttk.context.shared.domain.CargoId
import com.github.caay2000.ttk.context.shared.domain.StopId
import com.github.caay2000.ttk.context.shared.domain.WorldId
import com.github.caay2000.ttk.context.vehicle.world.domain.World
import com.github.caay2000.ttk.context.vehicle.world.domain.WorldRepository

class ConsumeCargoService(private val worldRepository: WorldRepository) {

    fun invoke(worldId: WorldId, stopId: StopId, cargoId: CargoId): Either<ConsumeCargoServiceException, Unit> =
        findWorld(worldId)
            .flatMap { world -> world.guardCargoExists(stopId, cargoId) }
            .map { world -> world.consumeCargo(stopId, cargoId) }
            .flatMap { world -> world.save() }
            .mapLeft { error -> error.mapError() }

    private fun findWorld(worldId: WorldId): Either<Throwable, World> =
        worldRepository.get(worldId).toEither { ConsumeCargoServiceException.WorldNotFound(worldId) }

    private fun World.guardCargoExists(stopId: StopId, cargoId: CargoId): Either<Throwable, World> =
        this.stops.find { stop -> stop.id == stopId }!!
            .let { stop ->
                if (stop.cargo.any { it.id == cargoId }) this.right()
                else ConsumeCargoServiceException.CargoNotFound(stop.id, cargoId).left()
            }

    private fun World.save() = worldRepository.save(this).map { }
}
