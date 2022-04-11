package com.github.caay2000.ttk.context.vehicle.application.repository

import arrow.core.Either
import arrow.core.Option
import com.github.caay2000.ttk.context.shared.domain.WorldId
import com.github.caay2000.ttk.context.vehicle.domain.World

interface WorldRepository {

    fun save(world: World): Either<Throwable, World>
    fun get(id: WorldId): Option<World>
}
