package com.github.caay2000.ttk.context.vehicle.vehicle.application.create

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import com.github.caay2000.ttk.context.shared.domain.StopId
import com.github.caay2000.ttk.context.shared.domain.VehicleId
import com.github.caay2000.ttk.context.shared.domain.VehicleType
import com.github.caay2000.ttk.context.shared.domain.WorldId
import com.github.caay2000.ttk.context.vehicle.configuration.application.find.FindVehicleConfigurationQuery
import com.github.caay2000.ttk.context.vehicle.configuration.application.find.FindVehicleConfigurationQueryResponse
import com.github.caay2000.ttk.context.vehicle.configuration.domain.VehicleConfiguration
import com.github.caay2000.ttk.context.vehicle.vehicle.domain.Vehicle
import com.github.caay2000.ttk.context.vehicle.vehicle.domain.VehicleRepository
import com.github.caay2000.ttk.context.vehicle.world.domain.Stop
import com.github.caay2000.ttk.context.vehicle.world.domain.WorldRepository
import com.github.caay2000.ttk.lib.eventbus.query.QueryBus

class VehicleCreatorService(
    private val queryBus: QueryBus,
    private val vehicleRepository: VehicleRepository,
    private val worldRepository: WorldRepository
) {

    // TODO vehicle creator should be created here in vehicle context, not projected from world context
    fun invoke(worldId: WorldId, stopId: StopId, vehicleId: VehicleId, vehicleType: VehicleType): Either<VehicleCreatorServiceException, Unit> =
        findVehicleConfiguration(vehicleType)
            .flatMap { configuration -> createVehicle(configuration, worldId, stopId, vehicleId, vehicleType) }
            .flatMap { vehicle -> vehicle.save() }
            .mapLeft { error -> error.mapError() }

    private fun findVehicleConfiguration(vehicleType: VehicleType): Either<Throwable, VehicleConfiguration> =
        queryBus.execute<FindVehicleConfigurationQuery, FindVehicleConfigurationQueryResponse>(FindVehicleConfigurationQuery(vehicleType)).right()
            .flatMap { response -> if (response.success) response.data.right() else VehicleCreatorServiceException.VehicleConfigurationNotFound(vehicleType).left() }
            .flatMap { data -> VehicleConfiguration.create(data!!.type, data.loadTime, data.speed, data.capacity).right() }

    private fun findStop(worldId: WorldId, stopId: StopId): Either<Throwable, Stop> =
        worldRepository.getStop(worldId, stopId).toEither { VehicleCreatorServiceException.StopNotFound(stopId) }

    private fun createVehicle(configuration: VehicleConfiguration, worldId: WorldId, stopId: StopId, id: VehicleId, type: VehicleType) =
        findStop(worldId, stopId)
            .flatMap { stop -> Vehicle.create(configuration, worldId, id, type, stop).right() }

    private fun Vehicle.save() = vehicleRepository.save(this).map { }
}
