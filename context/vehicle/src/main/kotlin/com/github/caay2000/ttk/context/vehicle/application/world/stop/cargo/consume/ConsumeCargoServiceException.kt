package com.github.caay2000.ttk.context.vehicle.application.world.stop.cargo.consume

import com.github.caay2000.ttk.context.shared.domain.CargoId
import com.github.caay2000.ttk.context.shared.domain.StopId

sealed class ConsumeCargoServiceException : Throwable {
    constructor(message: String) : super(message)
    constructor(cause: Throwable) : super(cause)

    data class StopNotFound(val stopId: StopId) : ConsumeCargoServiceException("stop ${stopId.rawId} not found")
    data class CargoNotFound(val stopId: StopId, val cargoId: CargoId) : ConsumeCargoServiceException("cargoId ${stopId.rawId} not found in stop ${stopId.rawId}")
    data class Unknown(override val cause: Throwable) : ConsumeCargoServiceException(cause)
}

internal fun Throwable.mapError() =
    when (this) {
        is ConsumeCargoServiceException.StopNotFound -> this
        is ConsumeCargoServiceException.CargoNotFound -> this
        else -> ConsumeCargoServiceException.Unknown(this)
    }
