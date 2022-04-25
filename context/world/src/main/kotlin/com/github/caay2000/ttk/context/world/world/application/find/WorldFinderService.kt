package com.github.caay2000.ttk.context.world.world.application.find

import arrow.core.Either
import com.github.caay2000.ttk.context.shared.domain.WorldId
import com.github.caay2000.ttk.context.world.world.domain.World
import com.github.caay2000.ttk.context.world.world.domain.WorldRepository

class WorldFinderService(private val worldRepository: WorldRepository) {

    fun invoke(worldId: WorldId): Either<WorldFinderServiceException, World> =
        worldRepository.get(worldId)
            .toEither { WorldFinderServiceException.WorldNotFound(worldId) }
}
