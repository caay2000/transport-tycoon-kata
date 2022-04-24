package com.github.caay2000.ttk.context.world.application.vehicle.update

import com.github.caay2000.ttk.context.shared.domain.WorldId

sealed class VehicleUpdaterServiceException : Throwable {
    constructor(message: String) : super(message)
    constructor(cause: Throwable) : super(cause)

    data class WorldNotFoundException(val worldId: WorldId) : VehicleUpdaterServiceException("world ${worldId.rawId} not found")
    data class Unknown(override val cause: Throwable) : VehicleUpdaterServiceException(cause)
}

internal fun Throwable.mapError() =
    when (this) {
        is VehicleUpdaterServiceException.WorldNotFoundException -> this
        else -> VehicleUpdaterServiceException.Unknown(this)
    }
