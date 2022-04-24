package com.github.caay2000.ttk.context.vehicle.application.vehicle.create

import com.github.caay2000.ttk.context.shared.domain.StopId

sealed class VehicleCreatorServiceException : Throwable {
    constructor(message: String) : super(message)
    constructor(cause: Throwable) : super(cause)

    data class StopNotFound(val stopId: StopId) : VehicleCreatorServiceException("stop ${stopId.rawId} not found")
    data class Unknown(override val cause: Throwable) : VehicleCreatorServiceException(cause)
}

internal fun Throwable.mapError() =
    when (this) {
        is VehicleCreatorServiceException.StopNotFound -> this
        else -> VehicleCreatorServiceException.Unknown(this)
    }
