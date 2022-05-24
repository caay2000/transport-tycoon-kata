package com.github.caay2000.ttk.context.vehicle.route.application.find

import arrow.core.Either
import arrow.core.computations.ResultEffect.bind
import arrow.core.computations.option
import arrow.core.flatMap
import arrow.core.getOrElse
import arrow.core.left
import arrow.core.right
import arrow.core.toOption
import com.github.caay2000.ttk.context.shared.domain.CargoId
import com.github.caay2000.ttk.context.shared.domain.Distance
import com.github.caay2000.ttk.context.shared.domain.StopId
import com.github.caay2000.ttk.context.shared.domain.VehicleId
import com.github.caay2000.ttk.context.shared.domain.VehicleTypeEnum
import com.github.caay2000.ttk.context.shared.domain.WorldId
import com.github.caay2000.ttk.context.vehicle.vehicle.domain.Vehicle
import com.github.caay2000.ttk.context.vehicle.vehicle.domain.VehicleRepository
import com.github.caay2000.ttk.context.vehicle.vehicle.domain.VehicleStatus
import com.github.caay2000.ttk.context.vehicle.world.domain.Stop
import com.github.caay2000.ttk.context.vehicle.world.domain.WorldRepository

class RouteFinderService(
    private val vehicleRepository: VehicleRepository,
    private val worldRepository: WorldRepository
) {

    fun invoke(vehicleId: VehicleId): Either<Throwable, RouteFinderResponse> =
        findVehicle(vehicleId)
            .flatMap { vehicle -> vehicle.guardVehicleIsIdle() }
            .flatMap { vehicle -> getStop(vehicle.worldId, vehicle.depotStopId) }
            .flatMap { stop -> stop.guardStopHasCargo() }
            .flatMap { stop -> stop.findRoute(vehicleId) }
            .mapLeft { error -> error.mapError() }

    private fun findVehicle(vehicleId: VehicleId): Either<Throwable, Vehicle> =
        vehicleRepository.get(vehicleId)
            .mapLeft { RouteFinderServiceException.VehicleNotFound(vehicleId) }

    private fun Vehicle.guardVehicleIsIdle(): Either<Throwable, Vehicle> =
        if (this.status != VehicleStatus.IDLE) RouteFinderServiceException.VehicleInvalidStatus(this.id, this.status, VehicleStatus.IDLE).left()
        else this.right()

    private fun getStop(worldId: WorldId, stopId: StopId): Either<Throwable, Stop> =
        worldRepository.getStop(worldId, stopId).toEither { RouteFinderServiceException.StopNotFound(stopId) }

    private fun Stop.guardStopHasCargo(): Either<Throwable, Stop> =
        if (this.cargo.none { it.reserved.not() }) RouteFinderServiceException.StopHasNoCargo(this.id).left()
        else this.right()

    private fun Stop.findRoute(vehicleId: VehicleId): Either<Throwable, RouteFinderResponse> =
        this.cargo.first { it.reserved.not() }.right()
            .flatMap { cargo ->
                val world = worldRepository.get(this.worldId).getOrElse { throw RouteFinderServiceException.WorldNotFound(worldId) }
                if (this.name != "FACTORY") {
                    val distance = world.distance(this.id, cargo.targetId)
                    RouteFinderResponse(cargo.id, cargo.sourceId, cargo.targetId, cargo.targetId, distance).right()
                } else {
                    option.eager<StopId> {
                        val target = world.getStop(cargo.targetId)
                        val vehicle = vehicleRepository.get(vehicleId).bind()
                        if (target.name == "WAREHOUSE_A" && vehicle.type.type == VehicleTypeEnum.TRUCK) {
                            world.stops.find { it.name == "PORT" }!!.id
                        } else {
                            cargo.targetId
                        }
                    }.flatMap { targetStopId ->
                        val distance = world.distance(this.id, targetStopId)
                        RouteFinderResponse(cargo.id, cargo.sourceId, cargo.targetId, targetStopId, distance).toOption()
                    }.toEither { RouteFinderServiceException.Unknown(RuntimeException("Error handling route finder")) }
                }
            }

    data class RouteFinderResponse(
        val cargoId: CargoId,
        val cargoSourceStopId: StopId,
        val cargoTargetStopId: StopId,
        val routeTargetStopId: StopId,
        val routeTargetStopDistance: Distance
    )
}