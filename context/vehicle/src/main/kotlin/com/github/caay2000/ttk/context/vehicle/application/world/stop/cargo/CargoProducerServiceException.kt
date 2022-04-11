package com.github.caay2000.ttk.context.vehicle.application.world.stop.cargo

import com.github.caay2000.ttk.context.shared.domain.WorldId

sealed class CargoProducerServiceException : Throwable {
    constructor(message: String)
    constructor(cause: Throwable)

    data class WorldDoesNotExists(val worldId: WorldId) : CargoProducerServiceException("world does not exists ${worldId.rawId}")
    data class Unknown(override val cause: Throwable) : CargoProducerServiceException(cause)
}

internal fun Throwable.mapError() =
    when (this) {
        is CargoProducerServiceException.WorldDoesNotExists -> this
        else -> CargoProducerServiceException.Unknown(this)
    }
