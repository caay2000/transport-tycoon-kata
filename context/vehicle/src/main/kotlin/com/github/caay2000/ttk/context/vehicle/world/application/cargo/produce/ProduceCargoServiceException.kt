package com.github.caay2000.ttk.context.vehicle.world.application.cargo.produce

import com.github.caay2000.ttk.context.shared.domain.WorldId

sealed class ProduceCargoServiceException : Throwable {
    constructor(message: String) : super(message)
    constructor(cause: Throwable) : super(cause)

    data class WorldNotFound(val worldId: WorldId) : ProduceCargoServiceException("world ${worldId.rawId} not found")
    data class Unknown(override val cause: Throwable) : ProduceCargoServiceException(cause)
}

internal fun Throwable.mapError() =
    when (this) {
        is ProduceCargoServiceException.WorldNotFound -> this
        else -> ProduceCargoServiceException.Unknown(this)
    }
