package com.github.caay2000.ttk.context.vehicle.application.world.stop.cargo.consume

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import com.github.caay2000.ttk.context.shared.domain.CargoId
import com.github.caay2000.ttk.context.shared.domain.StopId
import com.github.caay2000.ttk.context.vehicle.domain.repository.StopRepository
import com.github.caay2000.ttk.context.vehicle.domain.world.Stop

class ConsumeCargoService(private val stopRepository: StopRepository) {

    fun invoke(stopId: StopId, cargoId: CargoId): Either<ConsumeCargoServiceException, Unit> =
        findStop(stopId)
            .flatMap { stop -> stop.guardCargoExists(cargoId) }
            .map { stop -> stop.consumeCargo(cargoId) }
            .flatMap { stop -> stop.save() }
            .mapLeft { error -> error.mapError() }

    private fun findStop(stopId: StopId): Either<Throwable, Stop> =
        stopRepository.get(stopId).toEither { ConsumeCargoServiceException.StopNotFound(stopId) }

    private fun Stop.guardCargoExists(cargoId: CargoId): Either<Throwable, Stop> =
        if (this.cargo.any { it.id == cargoId }) this.right()
        else ConsumeCargoServiceException.CargoNotFound(this.id, cargoId).left()

    private fun Stop.save() = stopRepository.save(this).map { }
}
