package com.github.caay2000.ttk.context.vehicle.route.application.find

import arrow.core.Either
import arrow.core.computations.ResultEffect.bind
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import com.github.caay2000.ttk.context.shared.domain.CargoId
import com.github.caay2000.ttk.context.shared.domain.Distance
import com.github.caay2000.ttk.context.shared.domain.StopId
import com.github.caay2000.ttk.context.shared.domain.VehicleId
import com.github.caay2000.ttk.context.shared.domain.WorldId
import com.github.caay2000.ttk.context.vehicle.vehicle.domain.Vehicle
import com.github.caay2000.ttk.context.vehicle.vehicle.domain.VehicleRepository
import com.github.caay2000.ttk.context.vehicle.vehicle.domain.VehicleStatus
import com.github.caay2000.ttk.context.vehicle.world.domain.Stop
import com.github.caay2000.ttk.context.vehicle.world.domain.World
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
        worldRepository.get(worldId).toEither { RouteFinderServiceException.WorldNotFound(worldId) }
            .flatMap { world -> world.findFirstValidRoute(this, vehicleRepository.get(vehicleId).bind()) }

    private fun World.findFirstValidRoute(stop: Stop, vehicle: Vehicle): Either<Throwable, RouteFinderResponse> =
        Either.catch {
            stop.cargo.first { cargo ->
                if (cargo.reserved.not()) {
                    val route = this.map.route(stop.id, cargo.targetId)
                    route.first().allowedVehicleTypes.contains(vehicle.type.type)
                } else false
            }.let { cargo ->
                val route = this.map.route(stop.id, cargo.targetId)
                RouteFinderResponse(cargo.id, cargo.sourceId, cargo.targetId, route.first().targetStopId, map.distance(stop.id, route.first().targetStopId))
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
