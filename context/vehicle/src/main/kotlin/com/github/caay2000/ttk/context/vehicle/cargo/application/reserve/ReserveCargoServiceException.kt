package com.github.caay2000.ttk.context.vehicle.cargo.application.reserve

import com.github.caay2000.ttk.context.shared.domain.StopId

sealed class ReserveCargoServiceException : Throwable {
    constructor(message: String) : super(message)
    constructor(cause: Throwable) : super(cause)

    data class StopNotFound(val stopId: StopId) : ReserveCargoServiceException("stop ${stopId.rawId} not found")
    data class Unknown(override val cause: Throwable) : ReserveCargoServiceException(cause)
}

internal fun Throwable.mapError() =
    when (this) {
        is ReserveCargoServiceException.StopNotFound -> this
        else -> ReserveCargoServiceException.Unknown(this)
    }
