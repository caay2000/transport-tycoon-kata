package com.github.caay2000.ttk.context.vehicle.world.application.create

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import com.github.caay2000.ttk.context.shared.domain.WorldId
import com.github.caay2000.ttk.context.vehicle.world.domain.World
import com.github.caay2000.ttk.context.vehicle.world.domain.WorldRepository

class WorldCreatorService(private val worldRepository: WorldRepository) {

    fun invoke(worldId: WorldId): Either<WorldCreatorServiceException, Unit> =
        guardWorldDoesNotExists(worldId)
            .flatMap { World.create(worldId).right() }
            .flatMap { world -> world.save() }
            .mapLeft { error -> error.mapError() }

    private fun guardWorldDoesNotExists(worldId: WorldId): Either<Throwable, Unit> =
        if (worldRepository.exists(worldId)) WorldCreatorServiceException.WorldAlreadyExists(worldId).left()
        else Unit.right()

    private fun World.save() = Either.catch { worldRepository.save(this) }.map { }
}
