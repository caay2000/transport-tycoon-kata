package com.github.caay2000.ttk.context.vehicle.application.vehicle.update

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.getOrHandle
import arrow.core.right
import com.github.caay2000.ttk.context.shared.domain.CargoId
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

    fun invoke(vehicleId: VehicleId): Either<Throwable, Unit> =
        findVehicle(vehicleId)
            .flatMap { vehicle -> vehicle.executeUpdate() }
            .flatMap { vehicle -> vehicle.save() }
            .flatMap { vehicle -> vehicle.publishEvents() }

    private fun findVehicle(vehicleId: VehicleId): Either<Throwable, Vehicle> =
        vehicleRepository.get(vehicleId).toEither { VehicleUpdaterServiceException.VehicleNotFound(vehicleId) }

    private fun Vehicle.executeUpdate(): Either<Throwable, Vehicle> {
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

        return this.right()
    }

    private fun FindRouteQueryResponse.toCargo(): Cargo =
        stopRepository.get(this.route!!.sourceId.toDomainId()).toEither { RuntimeException("") }
            .flatMap { stop -> stop.cargo.find { it.id == this.route.cargoId.toDomainId<CargoId>() }!!.right() }
            .getOrHandle { throw it }

    private fun FindRouteQueryResponse.toRoute(): Route =
        stopRepository.findDistanceBetween(this.route!!.sourceId.toDomainId(), this.route.targetId.toDomainId()).toEither { RuntimeException("") }
            .flatMap { distance -> Route(this.route.sourceId.toDomainId(), this.route.targetId.toDomainId(), distance).right() }
            .getOrHandle { throw it }
//
//        findStop(this.route!!.sourceId.toDomainId())
//            .flatMap { stop ->  }
//        findStop()
//            .flatMap { source -> stopRepository.get(this.route!!.sourceId.to()).toEither { VehicleUpdaterServiceException.StopNotFound(this.route!!.sourceId.toDomainId()) } }

    private fun findStop(stopId: StopId): Either<Throwable, Stop> =
        stopRepository.get(stopId).toEither { VehicleUpdaterServiceException.StopNotFound(stopId) }

//    Route(
//    id = this.route!!.cargoId.toDomainId(),
//    sourceId = this.route.sourceId.toDomainId(),
//    targetId = this.route.targetId.toDomainId()
//    )

    private fun Vehicle.save() = vehicleRepository.save(this).flatMap { this.right() }

    private fun Vehicle.publishEvents() = eventPublisher.publish(this.pullEvents()).right()
}
