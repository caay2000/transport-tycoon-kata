package com.github.caay2000.ttk.context.world.stop.domain

import arrow.core.Either
import arrow.core.Option
import com.github.caay2000.ttk.context.shared.domain.StopId
import com.github.caay2000.ttk.context.shared.domain.WorldId

interface StopRepository {

    fun save(stop: Stop): Either<Throwable, Stop>
    fun get(id: StopId): Option<Stop>
    fun exists(id: StopId): Boolean

    fun findAllByWorldId(id: WorldId): Set<Stop>
}
