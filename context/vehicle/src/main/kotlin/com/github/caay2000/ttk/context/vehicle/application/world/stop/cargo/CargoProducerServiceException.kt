package com.github.caay2000.ttk.context.vehicle.application.world.stop.cargo

import com.github.caay2000.ttk.context.shared.domain.StopId

sealed class CargoProducerServiceException : Throwable {
    constructor(message: String)
    constructor(cause: Throwable)

    data class StopDoesNotExists(val stopId: StopId) : CargoProducerServiceException("stop does not exists ${stopId.rawId}")
    data class Unknown(override val cause: Throwable) : CargoProducerServiceException(cause)
}

internal fun Throwable.mapError() =
    when (this) {
        is CargoProducerServiceException.StopDoesNotExists -> this
        else -> CargoProducerServiceException.Unknown(this)
    }
