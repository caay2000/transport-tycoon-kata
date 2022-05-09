package com.github.caay2000.ttk.context.vehicle.vehicle.application.create

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import com.github.caay2000.ttk.context.shared.domain.StopId
import com.github.caay2000.ttk.context.shared.domain.VehicleId
import com.github.caay2000.ttk.context.shared.domain.VehicleTypeEnum
import com.github.caay2000.ttk.context.shared.domain.WorldId
import com.github.caay2000.ttk.context.vehicle.configuration.application.find.FindVehicleConfigurationQuery
import com.github.caay2000.ttk.context.vehicle.configuration.application.find.FindVehicleConfigurationQueryResponse
import com.github.caay2000.ttk.context.vehicle.configuration.domain.VehicleConfiguration
import com.github.caay2000.ttk.context.vehicle.vehicle.domain.Vehicle
import com.github.caay2000.ttk.context.vehicle.vehicle.domain.VehicleRepository
import com.github.caay2000.ttk.lib.eventbus.query.QueryBus

class VehicleCreatorService(
    private val queryBus: QueryBus,
    private val vehicleRepository: VehicleRepository
) {

    // TODO vehicle creator should be created here in vehicle context, not projected from world context
    fun invoke(worldId: WorldId, stopId: StopId, vehicleId: VehicleId, vehicleType: VehicleTypeEnum): Either<VehicleCreatorServiceException, Unit> =
        findVehicleConfiguration(vehicleType)
            .flatMap { configuration -> Vehicle.create(configuration, worldId, vehicleId, vehicleType, stopId).right() }
            .flatMap { vehicle -> vehicle.save() }
            .mapLeft { error -> error.mapError() }

    private fun findVehicleConfiguration(vehicleType: VehicleTypeEnum): Either<Throwable, VehicleConfiguration> =
        queryBus.execute<FindVehicleConfigurationQuery, FindVehicleConfigurationQueryResponse>(FindVehicleConfigurationQuery(vehicleType)).right()
            .flatMap { response -> if (response.success) response.data.right() else VehicleCreatorServiceException.VehicleConfigurationNotFound(vehicleType).left() }
            .flatMap { data -> VehicleConfiguration.create(data!!.type, data.loadTime, data.speed, data.capacity).right() }

    private fun Vehicle.save() = vehicleRepository.save(this).map { }
}
