package com.github.caay2000.ttk.context.vehicle.world.application.cargo.reserve

import com.github.caay2000.ttk.context.shared.domain.WorldId

sealed class ReserveCargoServiceException : Throwable {
    constructor(message: String) : super(message)
    constructor(cause: Throwable) : super(cause)

    data class WorldNotFound(val worldId: WorldId) : ReserveCargoServiceException("world ${worldId.rawId} not found")
    data class Unknown(override val cause: Throwable) : ReserveCargoServiceException(cause)
}

internal fun Throwable.mapError() =
    when (this) {
        is ReserveCargoServiceException.WorldNotFound -> this
        else -> ReserveCargoServiceException.Unknown(this)
    }
