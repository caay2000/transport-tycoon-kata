package com.github.caay2000.ttk.context.vehicle.cargo.application.produce

import com.github.caay2000.ttk.context.shared.domain.StopId

sealed class ProduceCargoServiceException : Throwable {
    constructor(message: String) : super(message)
    constructor(cause: Throwable) : super(cause)

    data class StopNotFound(val stopId: StopId) : ProduceCargoServiceException("stop ${stopId.rawId} not found")
    data class Unknown(override val cause: Throwable) : ProduceCargoServiceException(cause)
}

internal fun Throwable.mapError() =
    when (this) {
        is ProduceCargoServiceException.StopNotFound -> this
        else -> ProduceCargoServiceException.Unknown(this)
    }
