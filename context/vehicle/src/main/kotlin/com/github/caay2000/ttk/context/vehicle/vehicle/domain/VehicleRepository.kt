package com.github.caay2000.ttk.context.vehicle.vehicle.domain

import arrow.core.Either
import com.github.caay2000.ttk.context.shared.domain.VehicleId

interface VehicleRepository {

    fun save(vehicle: Vehicle): Either<Throwable, Vehicle>
    fun get(id: VehicleId): Either<Throwable, Vehicle>
}
