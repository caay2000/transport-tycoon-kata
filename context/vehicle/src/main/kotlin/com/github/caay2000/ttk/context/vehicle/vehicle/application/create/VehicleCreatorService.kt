package com.github.caay2000.ttk.context.vehicle.vehicle.application.create

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.right
import com.github.caay2000.ttk.context.shared.domain.StopId
import com.github.caay2000.ttk.context.shared.domain.VehicleId
import com.github.caay2000.ttk.context.shared.domain.VehicleType
import com.github.caay2000.ttk.context.shared.domain.WorldId
import com.github.caay2000.ttk.context.vehicle.stop.domain.Stop
import com.github.caay2000.ttk.context.vehicle.stop.domain.StopRepository
import com.github.caay2000.ttk.context.vehicle.vehicle.domain.Vehicle
import com.github.caay2000.ttk.context.vehicle.vehicle.domain.VehicleRepository

class VehicleCreatorService(
    private val vehicleRepository: VehicleRepository,
    private val stopRepository: StopRepository
) {

    // TODO vehicle creator should be created here in vehicle context, not projected from world context
    fun invoke(worldId: WorldId, stopId: StopId, vehicleId: VehicleId, vehicleType: VehicleType): Either<VehicleCreatorServiceException, Unit> =
        findStop(stopId)
            .flatMap { stop -> createVehicle(worldId, stop, vehicleId, vehicleType) }
            .flatMap { vehicle -> vehicle.save() }
            .mapLeft { error -> error.mapError() }

    private fun findStop(stopId: StopId): Either<Throwable, Stop> =
        stopRepository.get(stopId).toEither { VehicleCreatorServiceException.StopNotFound(stopId) }

    private fun createVehicle(worldId: WorldId, stop: Stop, id: VehicleId, type: VehicleType) =
        Vehicle.create(worldId, id, type, stop).right()

    private fun Vehicle.save() = vehicleRepository.save(this).map { }
}
