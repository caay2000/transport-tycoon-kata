package com.github.caay2000.ttk.context.vehicle.application.service

import arrow.core.Either
import arrow.core.right
import com.github.caay2000.ttk.context.core.domain.VehicleId
import com.github.caay2000.ttk.context.vehicle.application.repository.VehicleRepository
import com.github.caay2000.ttk.context.vehicle.application.repository.WorldRepository

class VehicleUpdaterService(private val worldRepository: WorldRepository, private val vehicleRepository: VehicleRepository) {

    fun invoke(vehicleId: VehicleId): Either<Throwable, Unit> {

        return Unit.right()
    }
}
