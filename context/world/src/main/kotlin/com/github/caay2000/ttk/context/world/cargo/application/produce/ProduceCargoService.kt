package com.github.caay2000.ttk.context.world.cargo.application.produce

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.right
import com.github.caay2000.ttk.context.shared.domain.CargoId
import com.github.caay2000.ttk.context.shared.domain.StopId
import com.github.caay2000.ttk.context.world.cargo.domain.Cargo
import com.github.caay2000.ttk.context.world.stop.domain.Stop
import com.github.caay2000.ttk.context.world.stop.domain.StopRepository
import com.github.caay2000.ttk.lib.eventbus.event.Event
import com.github.caay2000.ttk.lib.eventbus.event.EventPublisher

class ProduceCargoService(
    private val eventPublisher: EventPublisher<Event>,
    private val stopRepository: StopRepository
) {

    fun invoke(stopId: StopId, cargoId: CargoId, targetId: StopId): Either<Throwable, Unit> =
        findStop(stopId)
            .map { stop -> stop.produceCargo(Cargo.create(cargoId, stopId, targetId)) }
            .flatMap { stop -> stop.save() }
            .flatMap { stop -> stop.publishEvents() }
            .mapLeft { error -> error.mapError() }

    private fun findStop(stopId: StopId): Either<Throwable, Stop> =
        stopRepository.get(stopId).toEither { ProduceCargoServiceException.StopNotFound(stopId) }

    private fun Stop.save() = stopRepository.save(this)

    private fun Stop.publishEvents() = eventPublisher.publish(this.pullEvents()).right()
}
