package com.github.caay2000.ttk.context.vehicle.configuration.secondary.database

import arrow.core.Either
import com.github.caay2000.ttk.context.shared.domain.VehicleTypeEnum
import com.github.caay2000.ttk.context.vehicle.configuration.domain.VehicleConfiguration
import com.github.caay2000.ttk.context.vehicle.configuration.domain.VehicleConfigurationRepository
import com.github.caay2000.ttk.lib.database.InMemoryDatabase

class InMemoryVehicleConfigurationRepository(private val database: InMemoryDatabase) : VehicleConfigurationRepository {

    // TODO create a DTO object for vehicle
    override fun save(configuration: VehicleConfiguration): Either<Throwable, VehicleConfiguration> =
        Either.catch { database.save(DATABASE_TABLE, configuration.type.name, configuration) as VehicleConfiguration }

    override fun get(type: VehicleTypeEnum): Either<Throwable, VehicleConfiguration> =
        Either.catch { database.getById(DATABASE_TABLE, type.name) as VehicleConfiguration }

    companion object {
        private const val DATABASE_TABLE = "VEHICLE_CTX_vehicle_configuration"
    }
}
