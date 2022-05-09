package com.github.caay2000.ttk.context.vehicle.world.application.connection.create

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.right
import com.github.caay2000.ttk.context.shared.domain.Distance
import com.github.caay2000.ttk.context.shared.domain.StopId
import com.github.caay2000.ttk.context.shared.domain.VehicleTypeEnum
import com.github.caay2000.ttk.context.shared.domain.WorldId
import com.github.caay2000.ttk.context.vehicle.world.domain.Connection
import com.github.caay2000.ttk.context.vehicle.world.domain.World
import com.github.caay2000.ttk.context.vehicle.world.domain.WorldRepository

class ConnectionCreatorService(private val worldRepository: WorldRepository) {

    fun invoke(
        worldId: WorldId,
        sourceStopId: StopId,
        targetStopId: StopId,
        distance: Distance,
        allowedVehicleTypes: Set<VehicleTypeEnum>
    ): Either<ConnectionCreatorServiceException, Unit> =
        findWorld(worldId)
            .flatMap { world -> world.createConnection(Connection(sourceStopId, targetStopId, distance, allowedVehicleTypes)).right() }
            .flatMap { world -> world.save() }
            .mapLeft { error -> error.mapError() }

    private fun findWorld(worldId: WorldId): Either<Throwable, World> =
        worldRepository.get(worldId).toEither { ConnectionCreatorServiceException.WorldNotFound(worldId) }

    private fun World.save() = worldRepository.save(this).map { }
}
