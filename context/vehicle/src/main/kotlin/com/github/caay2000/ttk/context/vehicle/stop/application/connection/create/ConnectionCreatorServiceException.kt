package com.github.caay2000.ttk.context.vehicle.stop.application.connection.create

import com.github.caay2000.ttk.context.shared.domain.StopId

sealed class ConnectionCreatorServiceException : Throwable {
    constructor(message: String) : super(message)
    constructor(cause: Throwable) : super(cause)

    data class StopNotFound(val stopId: StopId) : ConnectionCreatorServiceException("stop ${stopId.rawId} not found")
    data class Unknown(override val cause: Throwable) : ConnectionCreatorServiceException(cause)
}

internal fun Throwable.mapError() =
    when (this) {
        is ConnectionCreatorServiceException.StopNotFound -> this
        else -> ConnectionCreatorServiceException.Unknown(this)
    }
