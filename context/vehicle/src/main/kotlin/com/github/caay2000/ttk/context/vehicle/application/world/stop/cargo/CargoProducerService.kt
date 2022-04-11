package com.github.caay2000.ttk.context.vehicle.application.world.stop.cargo

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.right
import com.github.caay2000.ttk.context.shared.domain.CargoId
import com.github.caay2000.ttk.context.shared.domain.StopId
import com.github.caay2000.ttk.context.vehicle.application.repository.StopRepository
import com.github.caay2000.ttk.context.vehicle.domain.cargo.Cargo
import com.github.caay2000.ttk.context.vehicle.domain.world.Stop

class CargoProducerService(private val stopRepository: StopRepository) {

    fun invoke(cargoId: CargoId, sourceId: StopId, targetId: StopId): Either<Throwable, Unit> =
        findStop(sourceId)
            .flatMap { stop -> stop.produceCargo(cargoId, sourceId, targetId) }
            .flatMap { stop -> stop.save() }
            .mapLeft { error -> error.mapError() }

    private fun findStop(stopId: StopId) = stopRepository.get(stopId)
        .toEither { CargoProducerServiceException.StopDoesNotExists(stopId) }

    private fun Stop.produceCargo(cargoId: CargoId, sourceId: StopId, targetId: StopId): Either<Throwable, Stop> =
        this.copy(cargo = this.cargo + Cargo.create(cargoId, sourceId, targetId)).right()

    private fun Stop.save() = stopRepository.save(this).flatMap { Unit.right() }
}
