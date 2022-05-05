package com.github.caay2000.ttk.context.vehicle.configuration.application.find

import com.github.caay2000.ttk.context.shared.domain.VehicleType

sealed class VehicleConfigurationFinderServiceException : Throwable {
    constructor(message: String) : super(message)
    constructor(cause: Throwable) : super(cause)

    data class VehicleConfigurationNotFound(val type: VehicleType) : VehicleConfigurationFinderServiceException("vehicle configuration for $type not found")
    data class Unknown(override val cause: Throwable) : VehicleConfigurationFinderServiceException(cause)
}

internal fun Throwable.mapError() =
    when (this) {
        is VehicleConfigurationFinderServiceException.VehicleConfigurationNotFound -> this
        else -> VehicleConfigurationFinderServiceException.Unknown(this)
    }
