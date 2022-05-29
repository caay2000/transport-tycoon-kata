package com.github.caay2000.ttk.context.vehicle.vehicle.application.update

import arrow.core.Either
import arrow.core.Option
import arrow.core.flatMap
import arrow.core.right
import com.github.caay2000.ttk.context.shared.domain.VehicleId
import com.github.caay2000.ttk.context.vehicle.route.application.find.FindRouteQuery
import com.github.caay2000.ttk.context.vehicle.route.application.find.FindRouteQueryResponse
import com.github.caay2000.ttk.context.vehicle.vehicle.domain.Vehicle
import com.github.caay2000.ttk.context.vehicle.vehicle.domain.VehicleRepository
import com.github.caay2000.ttk.context.vehicle.vehicle.domain.VehicleStatus
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
            .flatMap { vehicle -> vehicle.executeUpdate(vehicle.searchRoute()) }
            .flatMap { vehicle -> vehicle.save() }
            .flatMap { vehicle -> vehicle.publishEvents() }
            .mapLeft { error -> error.mapError() }

    private fun findVehicle(vehicleId: VehicleId): Either<Throwable, Vehicle> =
        vehicleRepository.get(vehicleId)
            .mapLeft { VehicleUpdaterServiceException.VehicleNotFound(vehicleId) }

    private fun Vehicle.searchRoute(): Option<FindRouteQueryResponse.RouteQueryResponse> =
        if (this.status == VehicleStatus.IDLE) {
            queryBus.execute<FindRouteQuery, FindRouteQueryResponse>(FindRouteQuery(this.id.uuid))
                .let { Option.fromNullable(it.route) }
        } else Option.fromNullable(null)

    private fun Vehicle.executeUpdate(route: Option<FindRouteQueryResponse.RouteQueryResponse>): Either<Throwable, Vehicle> =
        this.update(route.orNull()).right()

    private fun Vehicle.save() = vehicleRepository.save(this).also { println("saving vehicle $this") }.flatMap { this.right() }

    private fun Vehicle.publishEvents() = eventPublisher.publish(this.pullEvents()).right()
}
