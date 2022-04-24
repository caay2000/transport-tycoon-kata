package com.github.caay2000.ttk.context.vehicle.application.vehicle.update

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.getOrHandle
import arrow.core.left
import arrow.core.right
import com.github.caay2000.ttk.context.shared.domain.CargoId
import com.github.caay2000.ttk.context.shared.domain.Distance
import com.github.caay2000.ttk.context.shared.domain.StopId
import com.github.caay2000.ttk.context.shared.domain.VehicleId
import com.github.caay2000.ttk.context.shared.domain.toDomainId
import com.github.caay2000.ttk.context.shared.event.VehicleUpdatedEvent
import com.github.caay2000.ttk.context.vehicle.application.route.FindRouteQuery
import com.github.caay2000.ttk.context.vehicle.application.route.FindRouteQueryResponse
import com.github.caay2000.ttk.context.vehicle.domain.cargo.Cargo
import com.github.caay2000.ttk.context.vehicle.domain.repository.StopRepository
import com.github.caay2000.ttk.context.vehicle.domain.repository.VehicleRepository
import com.github.caay2000.ttk.context.vehicle.domain.vehicle.Route
import com.github.caay2000.ttk.context.vehicle.domain.vehicle.Vehicle
import com.github.caay2000.ttk.context.vehicle.domain.vehicle.VehicleStatus
import com.github.caay2000.ttk.context.vehicle.domain.world.Stop
import com.github.caay2000.ttk.lib.eventbus.event.Event
import com.github.caay2000.ttk.lib.eventbus.event.EventPublisher
import com.github.caay2000.ttk.lib.eventbus.query.QueryBus

class VehicleUpdaterService(
    private val eventPublisher: EventPublisher<Event>,
    private val queryBus: QueryBus,
    private val vehicleRepository: VehicleRepository,
    private val stopRepository: StopRepository
) {

    fun invoke(vehicleId: VehicleId): Either<VehicleUpdaterServiceException, Unit> =
        findVehicle(vehicleId)
            .flatMap { vehicle -> vehicle.executeUpdate() }
            .flatMap { vehicle -> vehicle.save() }
            .flatMap { vehicle -> vehicle.publishEvents() }
            .mapLeft { error -> error.mapError() }

    private fun findVehicle(vehicleId: VehicleId): Either<Throwable, Vehicle> =
        vehicleRepository.get(vehicleId).toEither { VehicleUpdaterServiceException.VehicleNotFound(vehicleId) }

    private fun Vehicle.executeUpdate(): Either<Throwable, Vehicle> =
        Either.catch {
            when (this.status) {
                VehicleStatus.IDLE -> {
                    val queryResponse = queryBus.execute<FindRouteQuery, FindRouteQueryResponse>(FindRouteQuery(this.id.uuid))
                    if (queryResponse.routeFound) {
                        this.loadCargo(queryResponse.toCargo())
                        this.startRoute(queryResponse.toRoute())
                        this.move()
                    }
                    this.pushEvent(VehicleUpdatedEvent(this.worldId.uuid, this.id.uuid, this.type.name, this.cargo?.id?.uuid, this.status.name))
                }
                VehicleStatus.ON_ROUTE -> {
                    when {
                        this.route!!.isStoppedInDestination() -> {
                            this.unloadCargo()
                            this.move()
                            this.pushEvent(VehicleUpdatedEvent(this.worldId.uuid, this.id.uuid, this.type.name, this.cargo?.id?.uuid, this.status.name))
                        }
                        this.route!!.isFinished() -> {
                            this.stop()
                            this.executeUpdate()
                        }
                        else -> {
                            this.move()
                            this.pushEvent(VehicleUpdatedEvent(this.worldId.uuid, this.id.uuid, this.type.name, this.cargo?.id?.uuid, this.status.name))
                        }
                    }
                }
            }
            this
        }

    private fun FindRouteQueryResponse.toCargo(): Cargo =
        findStop(this.route!!.sourceStopId.toDomainId())
            .flatMap { stop -> stop.cargo.find { it.id == this.route.cargoId.toDomainId<CargoId>() }!!.right() }
            .getOrHandle { throw it }

    private fun FindRouteQueryResponse.toRoute(): Route =
        findStop(this.route!!.sourceStopId.toDomainId())
            .flatMap { stop -> stop.guardConnectionExists(this.route.targetStopId.toDomainId()) }
            .flatMap { stop -> stop.findDistanceTo(this.route.targetStopId.toDomainId()) }
            .flatMap { distance -> Route(this.route.sourceStopId.toDomainId(), this.route.targetStopId.toDomainId(), distance).right() }
            .getOrHandle { throw it }

    private fun findStop(stopId: StopId): Either<Throwable, Stop> =
        stopRepository.get(stopId).toEither { VehicleUpdaterServiceException.StopNotFound(stopId) }

    private fun Stop.guardConnectionExists(targetStopId: StopId): Either<Throwable, Stop> =
        if (this.connections.any { it.targetStopId == targetStopId }) this.right()
        else VehicleUpdaterServiceException.ConnectionNotFound(this.id, targetStopId).left()

    private fun Stop.findDistanceTo(targetStopId: StopId): Either<Throwable, Distance> =
        Either.catch { this.distanceTo(targetStopId) }

    private fun Vehicle.save() = vehicleRepository.save(this).flatMap { this.right() }

    private fun Vehicle.publishEvents() = eventPublisher.publish(this.pullEvents()).right()
}
