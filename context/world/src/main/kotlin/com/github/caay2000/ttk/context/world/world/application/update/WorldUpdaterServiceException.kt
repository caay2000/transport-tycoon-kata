package com.github.caay2000.ttk.context.world.world.application.update

import com.github.caay2000.ttk.context.shared.domain.WorldId

sealed class WorldUpdaterServiceException : Throwable {
    constructor(message: String) : super(message)
    constructor(cause: Throwable) : super(cause)

    data class WorldNotFoundException(val worldId: WorldId) : WorldUpdaterServiceException("world ${worldId.rawId} not found")
    data class Unknown(override val cause: Throwable) : WorldUpdaterServiceException(cause)
}

internal fun Throwable.mapError() =
    when (this) {
        is WorldUpdaterServiceException.WorldNotFoundException -> this
        else -> WorldUpdaterServiceException.Unknown(this)
    }
