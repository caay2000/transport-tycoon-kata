package com.github.caay2000.ttk.context.world.application.service

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.right
import com.github.caay2000.ttk.context.core.domain.CargoId
import com.github.caay2000.ttk.context.core.domain.VehicleId
import com.github.caay2000.ttk.context.core.domain.WorldId
import com.github.caay2000.ttk.context.world.application.repository.WorldRepository
import com.github.caay2000.ttk.context.world.domain.World
import com.github.caay2000.ttk.lib.event.WorldUpdatedEvent
import com.github.caay2000.ttk.lib.eventbus.event.Event
import com.github.caay2000.ttk.lib.eventbus.event.EventPublisher

class VehicleUpdaterService(
    private val eventPublisher: EventPublisher<Event>,
    private val worldRepository: WorldRepository
) {

    fun invoke(worldId: WorldId, vehicleId: VehicleId, cargoId: CargoId?, status: String) =
        worldRepository.get(worldId).right()
            .flatMap { world -> world.vehicleUpdated(vehicleId, cargoId, status) }
            .flatMap { world -> world.checkWorldFullyUpdated() }
            .flatMap { world -> world.save() }
            .flatMap { world -> world.publishEvents() }

    private fun World.vehicleUpdated(vehicleId: VehicleId, cargoId: CargoId?, status: String): Either<Throwable, World> {
        this.updateVehicle(vehicleId, cargoId, status)
        if (cargoId != null) {
            this.removeCargo(cargoId)
        }
        return this.right()
    }

    private fun World.checkWorldFullyUpdated(): Either<Throwable, World> {
        val updated = this.vehicles.first().updated
        val worldUpdated = this.vehicles.all { it.updated == updated }
        if (worldUpdated) {
            this.pushEvent(WorldUpdatedEvent(this.id.uuid, this.isCompleted()))
        }
        return this.right()
    }

    private fun World.save() = worldRepository.save(this).right()

    private fun World.publishEvents() = eventPublisher.publish(this.pullEvents()).right()
}
