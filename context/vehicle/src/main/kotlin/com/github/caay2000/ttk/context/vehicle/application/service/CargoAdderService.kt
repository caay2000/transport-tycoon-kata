package com.github.caay2000.ttk.context.vehicle.application.service

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.right
import com.github.caay2000.ttk.context.shared.domain.CargoId
import com.github.caay2000.ttk.context.shared.domain.StopId
import com.github.caay2000.ttk.context.shared.domain.WorldId
import com.github.caay2000.ttk.context.vehicle.application.repository.WorldRepository
import com.github.caay2000.ttk.context.vehicle.domain.Cargo
import com.github.caay2000.ttk.context.vehicle.domain.World

class CargoAdderService(private val worldRepository: WorldRepository) {

    fun invoke(worldId: WorldId, cargoId: CargoId, sourceId: StopId, targetId: StopId): Either<Throwable, Unit> =
        findWorld(worldId)
            .flatMap { world -> world.addCargo(cargoId, sourceId, targetId) }
            .flatMap { world -> world.save() }

    private fun findWorld(worldId: WorldId) = worldRepository.get(worldId).toEither { RuntimeException("") }

    private fun World.addCargo(cargoId: CargoId, sourceId: StopId, targetId: StopId): Either<Throwable, World> {
        val sourceStop = this.getStop(sourceId)
        val targetStop = this.getStop(targetId)
        sourceStop.addCargo(Cargo(cargoId, sourceStop.id, sourceStop.name, targetStop.id, targetStop.name))
        return this.right()
    }

    private fun World.save() = worldRepository.save(this).flatMap { Unit.right() }
}
