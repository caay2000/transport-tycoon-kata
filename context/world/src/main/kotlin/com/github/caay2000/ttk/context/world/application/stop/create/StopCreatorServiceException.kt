package com.github.caay2000.ttk.context.world.application.stop.create

import com.github.caay2000.ttk.context.shared.domain.StopId
import com.github.caay2000.ttk.context.shared.domain.WorldId

sealed class StopCreatorServiceException : Throwable {
    constructor(message: String) : super(message)
    constructor(cause: Throwable) : super(cause)

    data class WorldNotFound(val worldId: WorldId) : StopCreatorServiceException("world ${worldId.rawId} not found")
    data class StopAlreadyExists(val stopId: StopId) : StopCreatorServiceException("stop ${stopId.rawId} already exists")
    data class StopNotFound(val stopId: StopId) : StopCreatorServiceException("stop ${stopId.rawId} not found")
    data class Unknown(override val cause: Throwable) : StopCreatorServiceException(cause)
}

internal fun Throwable.mapError() =
    when (this) {
        is StopCreatorServiceException.WorldNotFound -> this
        is StopCreatorServiceException.StopAlreadyExists -> this
        else -> StopCreatorServiceException.Unknown(this)
    }
