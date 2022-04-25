package com.github.caay2000.ttk.context.vehicle.vehicle.secondary.database

import arrow.core.Either
import arrow.core.toOption
import com.github.caay2000.ttk.context.shared.domain.VehicleId
import com.github.caay2000.ttk.context.vehicle.vehicle.domain.Vehicle
import com.github.caay2000.ttk.context.vehicle.vehicle.domain.VehicleRepository
import com.github.caay2000.ttk.lib.database.InMemoryDatabase

class InMemoryVehicleRepository(private val database: InMemoryDatabase) : VehicleRepository {

    // TODO create a DTO object for vehicle

    override fun save(vehicle: Vehicle) = Either.catch { database.save(DATABASE_TABLE, vehicle.id.rawId, vehicle) as Vehicle }
    override fun get(id: VehicleId) = (database.getById(DATABASE_TABLE, id.rawId) as Vehicle).toOption()

    companion object {
        private const val DATABASE_TABLE = "VEHICLE_CTX_vehicle"
    }
}
