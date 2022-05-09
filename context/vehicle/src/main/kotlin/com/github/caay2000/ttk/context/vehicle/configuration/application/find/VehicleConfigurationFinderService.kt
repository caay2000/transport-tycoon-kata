package com.github.caay2000.ttk.context.vehicle.configuration.application.find

import arrow.core.Either
import com.github.caay2000.ttk.context.shared.domain.VehicleTypeEnum
import com.github.caay2000.ttk.context.vehicle.configuration.domain.VehicleConfiguration
import com.github.caay2000.ttk.context.vehicle.configuration.domain.VehicleConfigurationRepository

class VehicleConfigurationFinderService(
    private val vehicleConfigurationRepository: VehicleConfigurationRepository
) {

    fun invoke(vehicleType: VehicleTypeEnum): Either<VehicleConfigurationFinderServiceException, VehicleConfiguration> =
        vehicleConfigurationRepository.get(vehicleType)
            .mapLeft { VehicleConfigurationFinderServiceException.VehicleConfigurationNotFound(vehicleType) }
            .mapLeft { error -> error.mapError() }
}
