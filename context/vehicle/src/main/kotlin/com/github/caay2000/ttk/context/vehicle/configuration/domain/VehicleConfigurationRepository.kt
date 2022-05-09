package com.github.caay2000.ttk.context.vehicle.configuration.domain

import arrow.core.Either
import com.github.caay2000.ttk.context.shared.domain.VehicleTypeEnum

interface VehicleConfigurationRepository {

    fun save(configuration: VehicleConfiguration): Either<Throwable, VehicleConfiguration>
    fun get(type: VehicleTypeEnum): Either<Throwable, VehicleConfiguration>
}
