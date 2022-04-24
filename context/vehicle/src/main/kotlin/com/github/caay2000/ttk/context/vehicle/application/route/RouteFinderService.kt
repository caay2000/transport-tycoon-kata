package com.github.caay2000.ttk.context.vehicle.application.route

import arrow.core.Either
import arrow.core.computations.option
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import arrow.core.toOption
import com.github.caay2000.ttk.context.shared.domain.CargoId
import com.github.caay2000.ttk.context.shared.domain.StopId
import com.github.caay2000.ttk.context.shared.domain.VehicleId
import com.github.caay2000.ttk.context.shared.domain.VehicleType
import com.github.caay2000.ttk.context.vehicle.domain.repository.StopRepository
import com.github.caay2000.ttk.context.vehicle.domain.repository.VehicleRepository
import com.github.caay2000.ttk.context.vehicle.domain.vehicle.Vehicle
import com.github.caay2000.ttk.context.vehicle.domain.vehicle.VehicleStatus
import com.github.caay2000.ttk.context.vehicle.domain.world.Stop

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
        vehicleRepository.get(vehicleId).toEither { RouteFinderServiceException.VehicleNotFound(vehicleId) }

    private fun Vehicle.guardVehicleIsIdle(): Either<Throwable, Vehicle> =
        if (this.status != VehicleStatus.IDLE) RouteFinderServiceException.VehicleInvalidStatus(this.id, this.status, VehicleStatus.IDLE).left()
        else this.right()

    private fun getStop(stopId: StopId): Either<Throwable, Stop> = stopRepository.get(stopId)
        .toEither { RouteFinderServiceException.StopNotFound(stopId) }

    private fun Stop.guardStopHasCargo(): Either<Throwable, Stop> =
        if (this.cargo.isEmpty()) RouteFinderServiceException.StopHasNoCargo(this.id).left()
        else this.right()

    private fun Stop.findRoute(vehicleId: VehicleId): Either<Throwable, RouteFinderResponse> =
        this.cargo.first().right()
            .flatMap { cargo ->
                if (this.name != "FACTORY") {
                    RouteFinderResponse(vehicleId, cargo.id, this.id, cargo.targetId).right()
                } else {
                    option.eager<StopId> {
                        val target = stopRepository.get(cargo.targetId).bind()
                        val vehicle = vehicleRepository.get(vehicleId).bind()
                        if (target.name == "WAREHOUSE_A" && vehicle.type == VehicleType.TRUCK) {
                            stopRepository.findByName("PORT").bind().id
                        } else {
                            cargo.targetId
                        }
                    }.flatMap { targetStopId -> RouteFinderResponse(vehicleId, cargo.id, this.id, targetStopId).toOption() }
                        .toEither { RuntimeException("") }
                }
            }

    data class RouteFinderResponse(
        val vehicleId: VehicleId,
        val cargoId: CargoId,
        val sourceId: StopId,
        val targetId: StopId
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
