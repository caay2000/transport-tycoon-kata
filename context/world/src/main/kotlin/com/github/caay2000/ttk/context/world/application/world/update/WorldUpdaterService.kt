package com.github.caay2000.ttk.context.world.application.world.update

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.right
import com.github.caay2000.ttk.context.shared.domain.WorldId
import com.github.caay2000.ttk.context.world.domain.World
import com.github.caay2000.ttk.context.world.domain.repository.WorldRepository
import com.github.caay2000.ttk.lib.eventbus.event.Event
import com.github.caay2000.ttk.lib.eventbus.event.EventPublisher

class WorldUpdaterService(
    private val eventPublisher: EventPublisher<Event>,
    private val worldRepository: WorldRepository
) {

    fun invoke(worldId: WorldId): Either<WorldUpdaterServiceException, Unit> =
        findWorld(worldId)
            .flatMap { world -> world.updateVehicles() }
            .flatMap { world -> world.save() }
            .flatMap { world -> world.publishEvents() }
            .mapLeft { error -> error.mapError() }

    private fun findWorld(worldId: WorldId) = worldRepository.get(worldId)
        .toEither { WorldUpdaterServiceException.WorldNotFoundException(worldId) }

    private fun World.updateVehicles() = this.update().let { this.right() }

    private fun World.save() = worldRepository.save(this)

    private fun World.publishEvents() = eventPublisher.publish(this.pullEvents()).right()
}
