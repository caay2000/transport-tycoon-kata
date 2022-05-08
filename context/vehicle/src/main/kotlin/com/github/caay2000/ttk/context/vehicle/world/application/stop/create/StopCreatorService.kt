package com.github.caay2000.ttk.context.vehicle.world.application.stop.create

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.right
import com.github.caay2000.ttk.context.shared.domain.StopId
import com.github.caay2000.ttk.context.shared.domain.WorldId
import com.github.caay2000.ttk.context.vehicle.world.domain.Stop
import com.github.caay2000.ttk.context.vehicle.world.domain.World
import com.github.caay2000.ttk.context.vehicle.world.domain.WorldRepository

class StopCreatorService(private val worldRepository: WorldRepository) {

    fun invoke(worldId: WorldId, stopId: StopId, stopName: String): Either<StopCreatorServiceException, Unit> =
        findWorld(worldId)
            .flatMap { world -> world.createStop(worldId, stopId, stopName) }
            .flatMap { world -> world.save() }
            .mapLeft { error -> error.mapError() }

    private fun findWorld(worldId: WorldId) = worldRepository.get(worldId)
        .toEither { StopCreatorServiceException.WorldDoesNotExists(worldId) }

    private fun World.createStop(worldId: WorldId, stopId: StopId, stopName: String): Either<Nothing, World> =
        this.createStop(Stop.create(worldId, stopId, stopName)).right()

    private fun World.save() = worldRepository.save(this)
        .map { }
}
