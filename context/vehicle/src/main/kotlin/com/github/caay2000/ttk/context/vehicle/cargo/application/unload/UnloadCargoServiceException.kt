package com.github.caay2000.ttk.context.vehicle.cargo.application.unload

import com.github.caay2000.ttk.context.shared.domain.WorldId

sealed class UnloadCargoServiceException : Throwable {
    constructor(message: String) : super(message)
    constructor(cause: Throwable) : super(cause)

    data class WorldNotFound(val worldId: WorldId) : UnloadCargoServiceException("world ${worldId.rawId} not found")
    data class Unknown(override val cause: Throwable) : UnloadCargoServiceException(cause)
}

internal fun Throwable.mapError() =
    when (this) {
        is UnloadCargoServiceException.WorldNotFound -> this
        else -> UnloadCargoServiceException.Unknown(this)
    }
