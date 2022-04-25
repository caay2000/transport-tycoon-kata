package com.github.caay2000.ttk.context.vehicle.world.application.create

import com.github.caay2000.ttk.context.shared.domain.WorldId

sealed class WorldCreatorServiceException : Throwable {
    constructor(message: String) : super(message)
    constructor(cause: Throwable) : super(cause)

    data class WorldAlreadyExists(val worldId: WorldId) : WorldCreatorServiceException("world already exists ${worldId.rawId}")
    data class Unknown(override val cause: Throwable) : WorldCreatorServiceException(cause)
}

internal fun Throwable.mapError() =
    when (this) {
        is WorldCreatorServiceException.WorldAlreadyExists -> this
        else -> WorldCreatorServiceException.Unknown(this)
    }
