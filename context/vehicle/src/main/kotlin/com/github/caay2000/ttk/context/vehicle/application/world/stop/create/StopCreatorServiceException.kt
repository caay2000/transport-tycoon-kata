package com.github.caay2000.ttk.context.vehicle.application.world.stop.create

import com.github.caay2000.ttk.context.shared.domain.WorldId

sealed class StopCreatorServiceException : Throwable {
    constructor(message: String) : super(message)
    constructor(cause: Throwable) : super(cause)

    data class WorldDoesNotExists(val worldId: WorldId) : StopCreatorServiceException("world does not exists ${worldId.rawId}")
    data class Unknown(override val cause: Throwable) : StopCreatorServiceException(cause)
}

internal fun Throwable.mapError() =
    when (this) {
        is StopCreatorServiceException.WorldDoesNotExists -> this
        else -> StopCreatorServiceException.Unknown(this)
    }
