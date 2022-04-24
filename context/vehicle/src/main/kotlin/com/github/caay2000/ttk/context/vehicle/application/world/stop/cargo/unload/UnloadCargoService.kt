package com.github.caay2000.ttk.context.vehicle.application.world.stop.cargo.unload

import arrow.core.Either
import arrow.core.flatMap
import com.github.caay2000.ttk.context.shared.domain.CargoId
import com.github.caay2000.ttk.context.shared.domain.StopId
import com.github.caay2000.ttk.context.vehicle.domain.cargo.Cargo
import com.github.caay2000.ttk.context.vehicle.domain.repository.StopRepository
import com.github.caay2000.ttk.context.vehicle.domain.world.Stop

class UnloadCargoService(private val stopRepository: StopRepository) {

    fun invoke(stopId: StopId, cargoId: CargoId, sourceStopId: StopId, targetStopId: StopId): Either<Throwable, Unit> =
        findStop(stopId)
            .map { stop -> stop.unloadCargo(Cargo.create(cargoId, sourceStopId, targetStopId)) }
            .flatMap { stop -> stop.save() }

    private fun findStop(stopId: StopId): Either<Throwable, Stop> =
        stopRepository.get(stopId).toEither { UnloadCargoServiceException.StopNotFound(stopId) }

    private fun Stop.save() = stopRepository.save(this).map { }
}
