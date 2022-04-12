package com.github.caay2000.ttk.context.vehicle.application.repository

import arrow.core.Either
import arrow.core.Option
import com.github.caay2000.ttk.context.shared.domain.Distance
import com.github.caay2000.ttk.context.shared.domain.StopId
import com.github.caay2000.ttk.context.vehicle.domain.world.Stop

interface StopRepository {

    fun save(stop: Stop): Either<Throwable, Stop>
    fun get(id: StopId): Option<Stop>
    fun exists(id: StopId): Boolean

    fun findByName(name: String): Option<Stop>
    fun findDistanceBetween(sourceId: StopId, targetId: StopId): Option<Distance>
}
