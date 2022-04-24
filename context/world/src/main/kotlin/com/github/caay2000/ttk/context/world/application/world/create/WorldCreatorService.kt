package com.github.caay2000.ttk.context.world.application.world.create

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import com.github.caay2000.ttk.context.shared.domain.WorldId
import com.github.caay2000.ttk.context.world.domain.World
import com.github.caay2000.ttk.context.world.domain.repository.WorldRepository
import com.github.caay2000.ttk.lib.eventbus.event.Event
import com.github.caay2000.ttk.lib.eventbus.event.EventPublisher

class WorldCreatorService(
    private val eventPublisher: EventPublisher<Event>,
    private val worldRepository: WorldRepository
) {

    fun invoke(worldId: WorldId): Either<WorldCreatorServiceException, Unit> =
        guardWorldDoesNotExists(worldId)
            .flatMap { World.create(worldId).right() }
            .flatMap { world -> world.save() }
            .flatMap { world -> world.publishEvents() }
            .mapLeft { error -> error.mapError() }

    private fun guardWorldDoesNotExists(worldId: WorldId): Either<WorldCreatorServiceException, Unit> =
        if (worldRepository.exists(worldId)) WorldCreatorServiceException.WorldAlreadyExists(worldId).left()
        else Unit.right()

    private fun World.save() = worldRepository.save(this)
    private fun World.publishEvents() = eventPublisher.publish(this.pullEvents()).right()
}
