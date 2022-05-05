package com.github.caay2000.ttk.context.world.stop.application.connection.create

import com.github.caay2000.ttk.context.shared.domain.StopId
import com.github.caay2000.ttk.context.shared.domain.WorldId

sealed class ConnectionCreatorServiceException : Throwable {
    constructor(message: String) : super(message)
    constructor(cause: Throwable) : super(cause)

    data class WorldNotFound(val worldId: WorldId) : ConnectionCreatorServiceException("world ${worldId.rawId} not found")
    data class StopAlreadyExists(val stopId: StopId) : ConnectionCreatorServiceException("stop ${stopId.rawId} already exists")
    data class StopNotFound(val stopId: StopId) : ConnectionCreatorServiceException("stop ${stopId.rawId} not found")
    data class Unknown(override val cause: Throwable) : ConnectionCreatorServiceException(cause)
}

internal fun Throwable.mapError() =
    when (this) {
        is ConnectionCreatorServiceException.WorldNotFound -> this
        is ConnectionCreatorServiceException.StopAlreadyExists -> this
        else -> ConnectionCreatorServiceException.Unknown(this)
    }
