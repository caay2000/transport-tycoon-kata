package com.github.caay2000.ttk.context.world.vehicle.application.create

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import com.github.caay2000.ttk.context.shared.domain.StopId
import com.github.caay2000.ttk.context.shared.domain.VehicleId
import com.github.caay2000.ttk.context.shared.domain.VehicleTypeEnum
import com.github.caay2000.ttk.context.shared.domain.WorldId
import com.github.caay2000.ttk.context.world.vehicle.domain.Vehicle
import com.github.caay2000.ttk.context.world.world.domain.World
import com.github.caay2000.ttk.context.world.world.domain.WorldRepository
import com.github.caay2000.ttk.lib.eventbus.event.Event
import com.github.caay2000.ttk.lib.eventbus.event.EventPublisher

class VehicleCreatorService(private val eventPublisher: EventPublisher<Event>, private val worldRepository: WorldRepository) {

    fun invoke(worldId: WorldId, stopId: StopId, vehicleId: VehicleId, vehicleType: VehicleTypeEnum): Either<VehicleCreatorServiceException, Unit> =
        findWorld(worldId)
            .flatMap { world -> world.guardStopExists(stopId) }
            .map { world -> world.createVehicle(stopId, Vehicle.create(vehicleId, vehicleType)) }
            .flatMap { world -> world.save() }
            .flatMap { world -> world.publishEvents() }
            .mapLeft { error -> error.mapError() }

    private fun findWorld(worldId: WorldId) = worldRepository.get(worldId)
        .toEither { VehicleCreatorServiceException.WorldNotFoundException(worldId) }

    private fun World.guardStopExists(stopId: StopId): Either<Throwable, World> =
        if (this.stops.any { stop -> stop.id == stopId }) this.right()
        else VehicleCreatorServiceException.StopNotFoundException(stopId).left()

    private fun World.save() = worldRepository.save(this)
    private fun World.publishEvents() = eventPublisher.publish(this.pullEvents()).right()
}
