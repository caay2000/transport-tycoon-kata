package com.github.caay2000.ttk.context.vehicle.application.vehicle.update

import com.github.caay2000.ttk.context.shared.domain.StopId
import com.github.caay2000.ttk.context.shared.domain.VehicleId

sealed class VehicleUpdaterServiceException : Throwable {
    constructor(message: String) : super(message)
    constructor(cause: Throwable) : super(cause)

    data class VehicleNotFound(val vehicleId: VehicleId) : VehicleUpdaterServiceException("vehicle ${vehicleId.rawId} not found")
    data class StopNotFound(val stopId: StopId) : VehicleUpdaterServiceException("stop ${stopId.rawId} not found")
    data class ConnectionNotFound(val sourceStopId: StopId, val targetStopId: StopId) :
        VehicleUpdaterServiceException("connection from ${sourceStopId.rawId} to ${targetStopId.rawId} not found")

    data class Unknown(override val cause: Throwable) : VehicleUpdaterServiceException(cause)
}

internal fun Throwable.mapError() =
    when (this) {
        is VehicleUpdaterServiceException.VehicleNotFound -> this
        is VehicleUpdaterServiceException.StopNotFound -> this
        is VehicleUpdaterServiceException.ConnectionNotFound -> this
        else -> VehicleUpdaterServiceException.Unknown(this)
    }
