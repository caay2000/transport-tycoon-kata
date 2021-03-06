package com.github.caay2000.ttk.context.vehicle.route.application.find

import com.github.caay2000.ttk.context.shared.domain.StopId
import com.github.caay2000.ttk.context.shared.domain.VehicleId
import com.github.caay2000.ttk.context.shared.domain.WorldId
import com.github.caay2000.ttk.context.vehicle.vehicle.domain.VehicleStatus

sealed class RouteFinderServiceException : Throwable {
    constructor(message: String) : super(message)
    constructor(cause: Throwable) : super(cause)

    data class WorldNotFound(val worldId: WorldId) : RouteFinderServiceException("world ${worldId.rawId} not found")
    data class StopNotFound(val stopId: StopId) : RouteFinderServiceException("stop ${stopId.rawId} not found")
    data class StopHasNoCargo(val stopId: StopId) : RouteFinderServiceException("stop ${stopId.rawId} has no cargo")
    data class VehicleNotFound(val vehicleId: VehicleId) : RouteFinderServiceException("vehicle ${vehicleId.rawId} not found")
    data class VehicleInvalidStatus(val vehicleId: VehicleId, val currentStatus: VehicleStatus, val expectedStatus: VehicleStatus) :
        RouteFinderServiceException("vehicle ${vehicleId.rawId} has invalid status. current: $currentStatus expecting: $expectedStatus")

    data class Unknown(override val cause: Throwable) : RouteFinderServiceException(cause)
}

internal fun Throwable.mapError() =
    when (this) {
        is RouteFinderServiceException.StopNotFound -> this
        is RouteFinderServiceException.StopHasNoCargo -> this
        is RouteFinderServiceException.VehicleNotFound -> this
        is RouteFinderServiceException.VehicleInvalidStatus -> this
        else -> RouteFinderServiceException.Unknown(this)
    }
