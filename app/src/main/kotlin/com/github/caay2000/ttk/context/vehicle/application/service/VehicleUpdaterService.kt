package com.github.caay2000.ttk.context.vehicle.application.service

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.right
import com.github.caay2000.ttk.context.core.domain.VehicleId
import com.github.caay2000.ttk.context.core.event.EventPublisher
import com.github.caay2000.ttk.context.core.event.VehicleUpdatedEvent
import com.github.caay2000.ttk.context.vehicle.application.repository.VehicleRepository
import com.github.caay2000.ttk.context.vehicle.domain.Vehicle

class VehicleUpdaterService(private val eventPublisher: EventPublisher, private val vehicleRepository: VehicleRepository) {

    fun invoke(vehicleId: VehicleId): Either<Throwable, Unit> =
        vehicleRepository.get(vehicleId).toEither { RuntimeException("") }
            .flatMap { vehicle -> update(vehicle) }
            .flatMap { vehicle -> vehicle.save() }
            .flatMap { vehicle -> vehicle.publishEvents() }

    private fun update(vehicle: Vehicle): Either<Throwable, Vehicle> {
        vehicle.update()
        vehicle.pushEvent(VehicleUpdatedEvent(vehicle.worldId.uuid, vehicle.id.uuid, vehicle.type.name, vehicle.cargo?.id?.uuid, vehicle.status.name))
        return vehicle.right()
    }

    private fun Vehicle.save() = vehicleRepository.save(this).flatMap { this.right() }

    private fun Vehicle.publishEvents() = eventPublisher.publish(this.pullEvents()).right()
}
