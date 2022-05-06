package com.github.caay2000.ttk.context.vehicle.route.application.find

import arrow.core.Either
import arrow.core.computations.ResultEffect.bind
import arrow.core.computations.option
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import arrow.core.toOption
import com.github.caay2000.ttk.context.shared.domain.CargoId
import com.github.caay2000.ttk.context.shared.domain.Distance
import com.github.caay2000.ttk.context.shared.domain.StopId
import com.github.caay2000.ttk.context.shared.domain.VehicleId
import com.github.caay2000.ttk.context.shared.domain.VehicleType
import com.github.caay2000.ttk.context.vehicle.stop.domain.Stop
import com.github.caay2000.ttk.context.vehicle.stop.domain.StopRepository
import com.github.caay2000.ttk.context.vehicle.vehicle.domain.Vehicle
import com.github.caay2000.ttk.context.vehicle.vehicle.domain.VehicleRepository
import com.github.caay2000.ttk.context.vehicle.vehicle.domain.VehicleStatus

class RouteFinderService(
    private val vehicleRepository: VehicleRepository,
    private val stopRepository: StopRepository
) {

    fun invoke(vehicleId: VehicleId): Either<Throwable, RouteFinderResponse> =
        findVehicle(vehicleId)
            .flatMap { vehicle -> vehicle.guardVehicleIsIdle() }
            .flatMap { vehicle -> getStop(vehicle.initialStop.id) }
            .flatMap { stop -> stop.guardStopHasCargo() }
            .flatMap { stop -> stop.findRoute(vehicleId) }
            .mapLeft { error -> error.mapError() }

    private fun findVehicle(vehicleId: VehicleId): Either<Throwable, Vehicle> =
        vehicleRepository.get(vehicleId)
            .mapLeft { RouteFinderServiceException.VehicleNotFound(vehicleId) }

    private fun Vehicle.guardVehicleIsIdle(): Either<Throwable, Vehicle> =
        if (this.status != VehicleStatus.IDLE) RouteFinderServiceException.VehicleInvalidStatus(this.id, this.status, VehicleStatus.IDLE).left()
        else this.right()

    private fun getStop(stopId: StopId): Either<Throwable, Stop> = stopRepository.get(stopId)
        .toEither { RouteFinderServiceException.StopNotFound(stopId) }

    private fun Stop.guardStopHasCargo(): Either<Throwable, Stop> =
        if (this.cargo.none { it.reserved.not() }) RouteFinderServiceException.StopHasNoCargo(this.id).left()
        else this.right()

    private fun Stop.findRoute(vehicleId: VehicleId): Either<Throwable, RouteFinderResponse> =
        this.cargo.first { it.reserved.not() }.right()
            .flatMap { cargo ->
                if (this.name != "FACTORY") {
                    RouteFinderResponse(cargo.id, cargo.sourceId, cargo.targetId, cargo.targetId, this.distanceTo(cargo.targetId)).right()
                } else {
                    option.eager<StopId> {
                        val target = stopRepository.get(cargo.targetId).bind()
                        val vehicle = vehicleRepository.get(vehicleId).bind()
                        if (target.name == "WAREHOUSE_A" && vehicle.type == VehicleType.TRUCK) {
                            stopRepository.findByName("PORT").bind().id
                        } else {
                            cargo.targetId
                        }
                    }.flatMap { targetStopId -> RouteFinderResponse(cargo.id, cargo.sourceId, cargo.targetId, targetStopId, this.distanceTo(targetStopId)).toOption() }
                        .toEither { RuntimeException("") }
                }
            }

    data class RouteFinderResponse(
        val cargoId: CargoId,
        val cargoSourceStopId: StopId,
        val cargoTargetStopId: StopId,
        val routeTargetStopId: StopId,
        val routeTargetStopDistance: Distance
    )

    // THIS LOGIC IS MISSING
//
//    when (initialStop.name == "FACTORY") && cargo!!.targetId
//    stopRepository.findByName("FACTORY")
//    .flatMap { stop -> }
//
// //        val world = stopRepository.findByName().get(this.worldId).getOrElse { throw RuntimeException("WorldNotFound") }
//    return when {
//        initialStop == world.getStop("FACTORY") && cargo!!.targetStopName == "WAREHOUSE_A" -> world.getStop("PORT")
//        else -> world.getStop(cargo!!.targetStopName)
//    }
}
