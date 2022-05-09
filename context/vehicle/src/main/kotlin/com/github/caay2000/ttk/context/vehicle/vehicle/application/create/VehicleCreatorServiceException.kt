package com.github.caay2000.ttk.context.vehicle.vehicle.application.create

import com.github.caay2000.ttk.context.shared.domain.StopId
import com.github.caay2000.ttk.context.shared.domain.VehicleTypeEnum

sealed class VehicleCreatorServiceException : Throwable {
    constructor(message: String) : super(message)
    constructor(cause: Throwable) : super(cause)

    data class VehicleConfigurationNotFound(val type: VehicleTypeEnum) : VehicleCreatorServiceException("vehicle configuration for $type not found")
    data class StopNotFound(val stopId: StopId) : VehicleCreatorServiceException("stop ${stopId.rawId} not found")
    data class Unknown(override val cause: Throwable) : VehicleCreatorServiceException(cause)
}

internal fun Throwable.mapError() =
    when (this) {
        is VehicleCreatorServiceException.VehicleConfigurationNotFound -> this
        is VehicleCreatorServiceException.StopNotFound -> this
        else -> VehicleCreatorServiceException.Unknown(this)
    }
