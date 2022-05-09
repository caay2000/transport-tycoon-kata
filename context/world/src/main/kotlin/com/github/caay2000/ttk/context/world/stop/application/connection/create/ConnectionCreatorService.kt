package com.github.caay2000.ttk.context.world.stop.application.connection.create

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import com.github.caay2000.ttk.context.shared.domain.Distance
import com.github.caay2000.ttk.context.shared.domain.StopId
import com.github.caay2000.ttk.context.shared.domain.VehicleTypeEnum
import com.github.caay2000.ttk.context.shared.domain.WorldId
import com.github.caay2000.ttk.context.world.stop.domain.Stop
import com.github.caay2000.ttk.context.world.stop.domain.StopRepository
import com.github.caay2000.ttk.context.world.world.domain.WorldRepository
import com.github.caay2000.ttk.lib.eventbus.event.Event
import com.github.caay2000.ttk.lib.eventbus.event.EventPublisher

class ConnectionCreatorService(
    private val eventPublisher: EventPublisher<Event>,
    private val worldRepository: WorldRepository,
    private val stopRepository: StopRepository
) {

    fun invoke(
        worldId: WorldId,
        sourceStopId: StopId,
        targetStopId: StopId,
        distance: Distance,
        allowedVehicleTypeEnums: Set<VehicleTypeEnum>
    ): Either<ConnectionCreatorServiceException, Unit> =
        guardWorldExists(worldId)
            .flatMap { guardStopsExists(targetStopId) }
            .flatMap { findStop(sourceStopId) }
            .flatMap { stop -> stop.createConnection(targetStopId, distance, allowedVehicleTypeEnums).right() }
            .flatMap { stop -> stop.save() }
            .flatMap { stop -> stop.publishEvents() }
            .mapLeft { error -> error.mapError() }

    private fun guardWorldExists(worldId: WorldId): Either<ConnectionCreatorServiceException, Unit> =
        if (worldRepository.exists(worldId)) Unit.right()
        else ConnectionCreatorServiceException.WorldNotFound(worldId).left()

    private fun guardStopsExists(stopId: StopId): Either<ConnectionCreatorServiceException, Unit> =
        if (stopRepository.exists(stopId)) Unit.right()
        else ConnectionCreatorServiceException.StopNotFound(stopId).left()

    private fun findStop(stopId: StopId) = stopRepository.get(stopId)
        .toEither { ConnectionCreatorServiceException.StopNotFound(stopId) }

    private fun Stop.save() = stopRepository.save(this)

    private fun Stop.publishEvents() = eventPublisher.publish(this.pullEvents()).right()
}
