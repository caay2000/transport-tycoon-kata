package com.github.caay2000.ttk.context.vehicle.application.world.stop.cargo.produce

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.right
import com.github.caay2000.ttk.context.shared.domain.CargoId
import com.github.caay2000.ttk.context.shared.domain.StopId
import com.github.caay2000.ttk.context.vehicle.domain.cargo.Cargo
import com.github.caay2000.ttk.context.vehicle.domain.repository.StopRepository
import com.github.caay2000.ttk.context.vehicle.domain.world.Stop

class ProduceCargoService(private val stopRepository: StopRepository) {

    fun invoke(stopId: StopId, cargoId: CargoId, targetId: StopId): Either<Throwable, Unit> =
        findStop(stopId)
            .flatMap { stop -> stop.produceCargo(Cargo.create(cargoId, stopId, targetId)).right() }
            .flatMap { stop -> stop.save() }

    private fun findStop(stopId: StopId): Either<Throwable, Stop> =
        stopRepository.get(stopId).toEither { ProduceCargoServiceException.StopNotFound(stopId) }

    private fun Stop.save() = stopRepository.save(this).map { }
}
