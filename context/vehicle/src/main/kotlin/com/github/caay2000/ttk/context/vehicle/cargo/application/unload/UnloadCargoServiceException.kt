package com.github.caay2000.ttk.context.vehicle.cargo.application.unload

import com.github.caay2000.ttk.context.shared.domain.StopId

sealed class UnloadCargoServiceException : Throwable {
    constructor(message: String) : super(message)
    constructor(cause: Throwable) : super(cause)

    data class StopNotFound(val stopId: StopId) : UnloadCargoServiceException("stop ${stopId.rawId} not found")
    data class Unknown(override val cause: Throwable) : UnloadCargoServiceException(cause)
}

internal fun Throwable.mapError() =
    when (this) {
        is UnloadCargoServiceException.StopNotFound -> this
        else -> UnloadCargoServiceException.Unknown(this)
    }
