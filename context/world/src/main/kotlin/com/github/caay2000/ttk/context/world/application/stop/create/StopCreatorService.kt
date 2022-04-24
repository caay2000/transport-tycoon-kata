package com.github.caay2000.ttk.context.world.application.stop.create

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import com.github.caay2000.ttk.context.shared.domain.StopId
import com.github.caay2000.ttk.context.shared.domain.WorldId
import com.github.caay2000.ttk.context.world.domain.Stop
import com.github.caay2000.ttk.context.world.domain.repository.StopRepository
import com.github.caay2000.ttk.context.world.domain.repository.WorldRepository
import com.github.caay2000.ttk.lib.eventbus.event.Event
import com.github.caay2000.ttk.lib.eventbus.event.EventPublisher

class StopCreatorService(
    private val eventPublisher: EventPublisher<Event>,
    private val worldRepository: WorldRepository,
    private val stopRepository: StopRepository
) {

    fun invoke(worldId: WorldId, stopId: StopId, name: String) =
        guardWorldExists(worldId)
            .flatMap { guardStopDoesNotExists(stopId) }
            .flatMap { Stop.create(worldId, stopId, name).right() }
            .flatMap { stop -> stop.save() }
            .flatMap { stop -> stop.publishEvents() }
            .mapLeft { error -> error.mapError() }

    private fun guardWorldExists(worldId: WorldId): Either<StopCreatorServiceException, Unit> =
        if (worldRepository.exists(worldId)) Unit.right()
        else StopCreatorServiceException.WorldNotFound(worldId).left()

    private fun guardStopDoesNotExists(stopId: StopId): Either<StopCreatorServiceException, Unit> =
        if (stopRepository.exists(stopId)) StopCreatorServiceException.StopAlreadyExists(stopId).left()
        else Unit.right()

    private fun Stop.save() = stopRepository.save(this)

    private fun Stop.publishEvents() = eventPublisher.publish(this.pullEvents()).right()
}
