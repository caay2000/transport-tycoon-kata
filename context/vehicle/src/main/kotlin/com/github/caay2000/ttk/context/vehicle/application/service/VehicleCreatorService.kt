package com.github.caay2000.ttk.context.vehicle.application.service

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.right
import com.github.caay2000.ttk.context.shared.domain.VehicleId
import com.github.caay2000.ttk.context.shared.domain.VehicleType
import com.github.caay2000.ttk.context.shared.domain.WorldId
import com.github.caay2000.ttk.context.vehicle.application.repository.VehicleRepository
import com.github.caay2000.ttk.context.vehicle.application.repository.WorldRepository
import com.github.caay2000.ttk.context.vehicle.domain.vehicle.Vehicle
import com.github.caay2000.ttk.context.vehicle.domain.world.Stop
import com.github.caay2000.ttk.context.vehicle.domain.world.World
import com.github.caay2000.ttk.lib.eventbus.event.Event
import com.github.caay2000.ttk.lib.eventbus.event.EventPublisher

class VehicleCreatorService(
    private val eventPublisher: EventPublisher<Event>,
    private val worldRepository: WorldRepository,
    private val vehicleRepository: VehicleRepository
) {

    fun invoke(worldId: WorldId, vehicleId: VehicleId, vehicleType: VehicleType, stopName: String): Either<Throwable, Unit> =
        guardWorldExists(worldId)
            .flatMap { world -> createVehicle(worldId, vehicleId, vehicleType, world.getStop(stopName)) }
            .flatMap { vehicle -> vehicle.save() }
            .flatMap { vehicle -> vehicle.publishEvents() }

    private fun guardWorldExists(worldId: WorldId): Either<Throwable, World> =
        worldRepository.get(worldId).toEither { RuntimeException("") }

    private fun createVehicle(worldId: WorldId, id: VehicleId, type: VehicleType, stop: Stop) =
        Vehicle.create(worldId, id, type, stop).right()

    private fun Vehicle.save() = vehicleRepository.save(this).flatMap { this.right() }

    private fun Vehicle.publishEvents() = eventPublisher.publish(this.pullEvents()).right()
}
