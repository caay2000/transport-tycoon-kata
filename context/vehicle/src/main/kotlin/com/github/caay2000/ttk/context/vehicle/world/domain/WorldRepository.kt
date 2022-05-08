package com.github.caay2000.ttk.context.vehicle.world.domain

import arrow.core.Either
import arrow.core.Option
import com.github.caay2000.ttk.context.shared.domain.StopId
import com.github.caay2000.ttk.context.shared.domain.WorldId

interface WorldRepository {

    fun save(world: World): Either<Throwable, World>
    fun get(id: WorldId): Option<World>
    fun exists(id: WorldId): Boolean

    fun getStop(worldId: WorldId, stopId: StopId): Option<Stop>
}
