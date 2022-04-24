package com.github.caay2000.ttk.context.world.application.vehicle.create

import com.github.caay2000.ttk.context.shared.domain.StopId
import com.github.caay2000.ttk.context.shared.domain.WorldId

sealed class VehicleCreatorServiceException : Throwable {
    constructor(message: String) : super(message)
    constructor(cause: Throwable) : super(cause)

    data class WorldNotFoundException(val worldId: WorldId) : VehicleCreatorServiceException("world ${worldId.rawId} not found")
    data class StopNotFoundException(val stopId: StopId) : VehicleCreatorServiceException("stop ${stopId.rawId} not found")
    data class Unknown(override val cause: Throwable) : VehicleCreatorServiceException(cause)
}

internal fun Throwable.mapError() =
    when (this) {
        is VehicleCreatorServiceException.WorldNotFoundException -> this
        is VehicleCreatorServiceException.StopNotFoundException -> this
        else -> VehicleCreatorServiceException.Unknown(this)
    }
