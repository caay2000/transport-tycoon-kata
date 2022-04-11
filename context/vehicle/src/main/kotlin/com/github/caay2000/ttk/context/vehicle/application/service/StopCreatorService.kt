package com.github.caay2000.ttk.context.vehicle.application.service

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.right
import com.github.caay2000.ttk.context.shared.domain.StopId
import com.github.caay2000.ttk.context.shared.domain.WorldId
import com.github.caay2000.ttk.context.vehicle.application.repository.WorldRepository
import com.github.caay2000.ttk.context.vehicle.domain.Stop
import com.github.caay2000.ttk.context.vehicle.domain.World

class StopCreatorService(private val worldRepository: WorldRepository) {

    fun invoke(worldId: WorldId, stopId: StopId, stopName: String): Either<Throwable, Unit> =
        findWorld(worldId)
            .flatMap { world -> world.createStop(stopId, stopName) }
            .flatMap { world -> world.save() }

    private fun findWorld(worldId: WorldId) = worldRepository.get(worldId).toEither { RuntimeException("") }

    private fun World.createStop(stopId: StopId, stopName: String): Either<Nothing, World> {
        val world = this.addStop(Stop(stopId, stopName, mutableListOf()))
        return world.right()
    }

    private fun World.save() = worldRepository.save(this).flatMap { Unit.right() }
}
