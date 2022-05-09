package com.github.caay2000.ttk.context.vehicle.vehicle.application.update

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.right
import com.github.caay2000.ttk.context.shared.domain.VehicleId
import com.github.caay2000.ttk.context.shared.domain.toDomainId
import com.github.caay2000.ttk.context.shared.event.VehicleUpdatedEvent
import com.github.caay2000.ttk.context.vehicle.route.application.find.FindRouteQuery
import com.github.caay2000.ttk.context.vehicle.route.application.find.FindRouteQueryResponse
import com.github.caay2000.ttk.context.vehicle.vehicle.domain.Vehicle
import com.github.caay2000.ttk.context.vehicle.vehicle.domain.VehicleRepository
import com.github.caay2000.ttk.context.vehicle.vehicle.domain.VehicleStatus
import com.github.caay2000.ttk.context.vehicle.world.domain.Cargo
import com.github.caay2000.ttk.lib.eventbus.event.Event
import com.github.caay2000.ttk.lib.eventbus.event.EventPublisher
import com.github.caay2000.ttk.lib.eventbus.query.QueryBus

class VehicleUpdaterService(
    private val eventPublisher: EventPublisher<Event>,
    private val queryBus: QueryBus,
    private val vehicleRepository: VehicleRepository
) {

    fun invoke(vehicleId: VehicleId): Either<VehicleUpdaterServiceException, Unit> =
        findVehicle(vehicleId)
            .flatMap { vehicle -> vehicle.executeUpdate() }
            .flatMap { vehicle -> vehicle.save() }
            .flatMap { vehicle -> vehicle.publishEvents() }
            .mapLeft { error -> error.mapError() }

    private fun findVehicle(vehicleId: VehicleId): Either<Throwable, Vehicle> =
        vehicleRepository.get(vehicleId)
            .mapLeft { VehicleUpdaterServiceException.VehicleNotFound(vehicleId) }

    private fun Vehicle.executeUpdate(): Either<Throwable, Vehicle> =
        Either.catch {
            with(this.update()) {
                when (this.status) {
                    VehicleStatus.IDLE -> this.updateIdle()
                    VehicleStatus.LOADING -> this.updateLoading()
                    VehicleStatus.ON_ROUTE -> this.updateOnRoute()
                    VehicleStatus.UNLOADING -> this.updateUnloading()
                    VehicleStatus.RETURNING -> this.updateReturning()
                }
                if (this.taskFinished && this.status != VehicleStatus.IDLE) {
                    this.executeUpdate()
                } else {
                    this.pushEvent(VehicleUpdatedEvent(this.worldId.uuid, this.id.uuid, this.type.type.name, this.cargo?.id?.uuid, this.status.name))
                }
                this
            }
        }

    private fun Vehicle.updateIdle(): Either<Throwable, Vehicle> {
        val queryResponse = queryBus.execute<FindRouteQuery, FindRouteQueryResponse>(FindRouteQuery(this.id.uuid))
        if (queryResponse.routeFound) {
            this.loadCargo(queryResponse.toCargo(), queryResponse.route!!.routeTargetStopId.toDomainId(), queryResponse.route.routeTargetStopDistance)
        }
        return this.right()
    }

    private fun Vehicle.updateLoading(): Either<Throwable, Vehicle> {
        if (this.taskFinished) {
            this.finishLoadingCargo()
            this.startRoute()
        }
        return this.right()
    }

    private fun Vehicle.updateOnRoute(): Either<Throwable, Vehicle> {
        if (this.taskFinished) {
            this.unloadCargo()
        }
        return this.right()
    }

    private fun Vehicle.updateUnloading(): Either<Throwable, Vehicle> {
        if (this.taskFinished) {
            this.finishUnloadingCargo()
            this.returnRoute()
            this.updateIdle()
        }
        return this.right()
    }

    private fun Vehicle.updateReturning(): Either<Throwable, Vehicle> {
        if (this.taskFinished) {
            this.stop()
            updateIdle()
        }
        return this.right()
    }

    private fun FindRouteQueryResponse.toCargo(): Cargo =
        with(this.route!!) {
            Cargo.create(this.cargoId.toDomainId(), this.cargoSourceStopId.toDomainId(), this.cargoTargetStopId.toDomainId())
        }

    private fun Vehicle.save() = vehicleRepository.save(this).flatMap { this.right() }

    private fun Vehicle.publishEvents() = eventPublisher.publish(this.pullEvents()).right()
}
