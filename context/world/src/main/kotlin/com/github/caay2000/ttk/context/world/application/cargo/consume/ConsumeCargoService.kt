package com.github.caay2000.ttk.context.world.application.cargo.consume

import arrow.core.Either
import arrow.core.flatMap
import com.github.caay2000.ttk.context.shared.domain.CargoId
import com.github.caay2000.ttk.context.shared.domain.StopId
import com.github.caay2000.ttk.context.world.domain.Stop
import com.github.caay2000.ttk.context.world.domain.repository.StopRepository

class ConsumeCargoService(private val stopRepository: StopRepository) {

    fun invoke(stopId: StopId, cargoId: CargoId): Either<ConsumeCargoServiceException, Unit> =
        findStop(stopId)
            .map { stop -> stop.consumeCargo(cargoId) }
            .flatMap { stop -> stop.save() }
            .mapLeft { error -> error.mapError() }

    private fun findStop(stopId: StopId): Either<Throwable, Stop> =
        stopRepository.get(stopId).toEither { ConsumeCargoServiceException.StopNotFound(stopId) }

    private fun Stop.save() = stopRepository.save(this).map { }
}
