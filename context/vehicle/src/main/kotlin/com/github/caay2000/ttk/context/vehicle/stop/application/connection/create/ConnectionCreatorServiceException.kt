package com.github.caay2000.ttk.context.vehicle.stop.application.connection.create

import com.github.caay2000.ttk.context.shared.domain.WorldId

sealed class ConnectionCreatorServiceException : Throwable {
    constructor(message: String) : super(message)
    constructor(cause: Throwable) : super(cause)

    data class WorldNotFound(val worldId: WorldId) : ConnectionCreatorServiceException("world ${worldId.rawId} not found")
    data class Unknown(override val cause: Throwable) : ConnectionCreatorServiceException(cause)
}

internal fun Throwable.mapError() =
    when (this) {
        is ConnectionCreatorServiceException.WorldNotFound -> this
        else -> ConnectionCreatorServiceException.Unknown(this)
    }
