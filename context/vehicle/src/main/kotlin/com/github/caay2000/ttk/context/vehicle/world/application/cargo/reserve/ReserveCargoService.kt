package com.github.caay2000.ttk.context.vehicle.world.application.cargo.reserve

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.right
import com.github.caay2000.ttk.context.shared.domain.CargoId
import com.github.caay2000.ttk.context.shared.domain.StopId
import com.github.caay2000.ttk.context.shared.domain.WorldId
import com.github.caay2000.ttk.context.vehicle.world.domain.World
import com.github.caay2000.ttk.context.vehicle.world.domain.WorldRepository

class ReserveCargoService(private val worldRepository: WorldRepository) {

    fun invoke(worldId: WorldId, stopId: StopId, cargoId: CargoId): Either<Throwable, Unit> =
        findWorld(worldId)
            .flatMap { world -> world.reserveCargo(stopId, cargoId).right() }
            .flatMap { world -> world.save() }

    private fun findWorld(worldId: WorldId): Either<Throwable, World> =
        worldRepository.get(worldId).toEither { ReserveCargoServiceException.WorldNotFound(worldId) }

    private fun World.save() = worldRepository.save(this).map { }
}
