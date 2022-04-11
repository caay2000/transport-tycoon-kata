package com.github.caay2000.ttk.context.vehicle.application.world.stop.cargo

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.right
import com.github.caay2000.ttk.context.shared.domain.CargoId
import com.github.caay2000.ttk.context.shared.domain.StopId
import com.github.caay2000.ttk.context.shared.domain.WorldId
import com.github.caay2000.ttk.context.vehicle.application.repository.WorldRepository
import com.github.caay2000.ttk.context.vehicle.domain.cargo.Cargo
import com.github.caay2000.ttk.context.vehicle.domain.world.World

class CargoProducerService(private val worldRepository: WorldRepository) {

    fun invoke(worldId: WorldId, cargoId: CargoId, sourceId: StopId, targetId: StopId): Either<Throwable, Unit> =
        findWorld(worldId)
            .flatMap { world -> world.produceCargo(cargoId, sourceId, targetId) }
            .flatMap { world -> world.save() }

    private fun findWorld(worldId: WorldId) = worldRepository.get(worldId)
        .toEither { CargoProducerServiceException.WorldDoesNotExists(worldId) }

    private fun World.produceCargo(cargoId: CargoId, sourceId: StopId, targetId: StopId): Either<Throwable, World> {
        val sourceStop = this.getStop(sourceId)
        val targetStop = this.getStop(targetId)
        sourceStop.addCargo(Cargo(cargoId, sourceStop.id, sourceStop.name, targetStop.id, targetStop.name))
        return this.right()
    }

    private fun World.save() = Either.catch { worldRepository.save(this) }.flatMap { Unit.right() }
}
