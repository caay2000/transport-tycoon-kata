package com.github.caay2000.ttk.context.vehicle.configuration.application.create

sealed class VehicleConfigurationCreatorServiceException : Throwable {
    constructor(message: String) : super(message)
    constructor(cause: Throwable) : super(cause)

    data class Unknown(override val cause: Throwable) : VehicleConfigurationCreatorServiceException(cause)
}

internal fun Throwable.mapError() = VehicleConfigurationCreatorServiceException.Unknown(this)
