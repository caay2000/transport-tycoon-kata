package com.github.caay2000.ttk.context.vehicle.application.world.stop.connection.create

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.right
import com.github.caay2000.ttk.context.shared.domain.Distance
import com.github.caay2000.ttk.context.shared.domain.StopId
import com.github.caay2000.ttk.context.shared.domain.VehicleType
import com.github.caay2000.ttk.context.vehicle.domain.repository.StopRepository
import com.github.caay2000.ttk.context.vehicle.domain.world.Stop

class ConnectionCreatorService(private val stopRepository: StopRepository) {

    fun invoke(
        sourceStopId: StopId,
        targetStopId: StopId,
        distance: Distance,
        allowedVehicleTypes: Set<VehicleType>
    ): Either<ConnectionCreatorServiceException, Unit> =
        findStop(sourceStopId)
            .flatMap { stop -> stop.createConnection(targetStopId, distance, allowedVehicleTypes).right() }
            .flatMap { stop -> stop.save() }
            .mapLeft { error -> error.mapError() }

    private fun findStop(stopId: StopId) = stopRepository.get(stopId)
        .toEither { ConnectionCreatorServiceException.StopNotFound(stopId) }

    private fun Stop.save() = stopRepository.save(this).map { }
}
