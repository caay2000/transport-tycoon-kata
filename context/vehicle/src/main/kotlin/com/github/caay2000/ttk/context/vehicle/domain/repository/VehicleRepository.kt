package com.github.caay2000.ttk.context.vehicle.domain.repository

import arrow.core.Either
import arrow.core.Option
import com.github.caay2000.ttk.context.shared.domain.VehicleId
import com.github.caay2000.ttk.context.vehicle.domain.vehicle.Vehicle

interface VehicleRepository {

    fun save(vehicle: Vehicle): Either<Throwable, Vehicle>
    fun get(id: VehicleId): Option<Vehicle>
}
