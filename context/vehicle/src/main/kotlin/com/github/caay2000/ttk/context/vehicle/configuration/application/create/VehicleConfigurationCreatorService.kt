package com.github.caay2000.ttk.context.vehicle.configuration.application.create

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.right
import com.github.caay2000.ttk.context.shared.domain.VehicleType
import com.github.caay2000.ttk.context.vehicle.configuration.domain.VehicleConfiguration
import com.github.caay2000.ttk.context.vehicle.configuration.domain.VehicleConfigurationRepository

class VehicleConfigurationCreatorService(
    private val vehicleConfigurationRepository: VehicleConfigurationRepository
) {

    fun invoke(type: VehicleType, loadTime: Int, speed: Double, capacity: Int): Either<VehicleConfigurationCreatorServiceException, Unit> =
        VehicleConfiguration.create(type, loadTime, speed, capacity).right()
            .flatMap { configuration -> configuration.save() }
            .mapLeft { error -> error.mapError() }

    private fun VehicleConfiguration.save() = vehicleConfigurationRepository.save(this).map { }
}
