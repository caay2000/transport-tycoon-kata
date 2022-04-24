package com.github.caay2000.ttk.context.vehicle.application.world.stop.create

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.right
import com.github.caay2000.ttk.context.shared.domain.StopId
import com.github.caay2000.ttk.context.shared.domain.WorldId
import com.github.caay2000.ttk.context.vehicle.domain.repository.WorldRepository
import com.github.caay2000.ttk.context.vehicle.domain.world.Stop
import com.github.caay2000.ttk.context.vehicle.domain.world.World

class StopCreatorService(private val worldRepository: WorldRepository) {

    fun invoke(worldId: WorldId, stopId: StopId, stopName: String): Either<StopCreatorServiceException, Unit> =
        findWorld(worldId)
            .flatMap { world -> world.createStop(worldId, stopId, stopName) }
            .flatMap { world -> world.save() }
            .mapLeft { error -> error.mapError() }

    private fun findWorld(worldId: WorldId) = worldRepository.get(worldId)
        .toEither { StopCreatorServiceException.WorldDoesNotExists(worldId) }

    private fun World.createStop(worldId: WorldId, stopId: StopId, stopName: String): Either<Nothing, World> =
        this.addStop(Stop.create(worldId, stopId, stopName)).right()

    private fun World.save() = worldRepository.save(this)
        .map { }
}
