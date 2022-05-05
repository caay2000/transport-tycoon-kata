package com.github.caay2000.ttk.context.world.vehicle.application.update

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.right
import com.github.caay2000.ttk.context.shared.domain.CargoId
import com.github.caay2000.ttk.context.shared.domain.VehicleId
import com.github.caay2000.ttk.context.shared.domain.WorldId
import com.github.caay2000.ttk.context.shared.event.WorldUpdatedEvent
import com.github.caay2000.ttk.context.world.world.domain.World
import com.github.caay2000.ttk.context.world.world.domain.WorldRepository
import com.github.caay2000.ttk.lib.eventbus.event.Event
import com.github.caay2000.ttk.lib.eventbus.event.EventPublisher

class VehicleUpdaterService(private val eventPublisher: EventPublisher<Event>, private val worldRepository: WorldRepository) {

    fun invoke(worldId: WorldId, vehicleId: VehicleId, cargoId: CargoId?): Either<VehicleUpdaterServiceException, Unit> =
        findWorld(worldId)
            .flatMap { world -> world.vehicleUpdated(vehicleId, cargoId) }
            .flatMap { world -> world.save() }
            .flatMap { world -> world.checkWorldFullyUpdated() }
            .flatMap { world -> world.publishEvents() }
            .mapLeft { error -> error.mapError() }

    private fun findWorld(worldId: WorldId) = worldRepository.get(worldId)
        .toEither { VehicleUpdaterServiceException.WorldNotFoundException(worldId) }

    private fun World.vehicleUpdated(vehicleId: VehicleId, cargoId: CargoId?): Either<Throwable, World> =
        this.updateVehicle(vehicleId, cargoId).right()

    private fun World.checkWorldFullyUpdated(): Either<Throwable, World> {
        val updated = this.vehicles.first().updated
        val worldUpdated = this.vehicles.all { it.updated == updated }
        if (worldUpdated) {
            this.pushEvent(WorldUpdatedEvent(this.id.uuid, this.isCompleted()))
        }
        return this.right()
    }

    private fun World.save() = worldRepository.save(this)

    private fun World.publishEvents() = eventPublisher.publish(this.pullEvents()).right()
}
