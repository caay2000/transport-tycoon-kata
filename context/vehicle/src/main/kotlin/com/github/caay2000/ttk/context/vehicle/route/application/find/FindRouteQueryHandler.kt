package com.github.caay2000.ttk.context.vehicle.route.application.find

import arrow.core.flatMap
import arrow.core.getOrHandle
import arrow.core.right
import com.github.caay2000.ttk.context.shared.domain.toDomainId
import com.github.caay2000.ttk.context.vehicle.stop.domain.StopRepository
import com.github.caay2000.ttk.context.vehicle.vehicle.domain.VehicleRepository
import com.github.caay2000.ttk.lib.eventbus.query.Query
import com.github.caay2000.ttk.lib.eventbus.query.QueryHandler
import java.util.UUID

class FindRouteQueryHandler(vehicleRepository: VehicleRepository, stopRepository: StopRepository) : QueryHandler<FindRouteQuery, FindRouteQueryResponse> {

    private val routeFinderService = RouteFinderService(vehicleRepository, stopRepository)

    override fun handle(query: FindRouteQuery): FindRouteQueryResponse =
        routeFinderService.invoke(query.vehicleId.toDomainId())
            .flatMap { response -> response.toFindRouteQueryResponse() }
            .getOrHandle { error -> error.toFindRouteQueryResponse() }

    private fun RouteFinderService.RouteFinderResponse.toFindRouteQueryResponse() =
        FindRouteQueryResponse(
            routeFound = true,
            route = FindRouteQueryResponse.RouteQueryResponse(
                this.cargoId.uuid,
                this.cargoSourceStopId.uuid,
                this.cargoTargetStopId.uuid,
                this.routeTargetStopId.uuid,
                this.routeTargetStopDistance
            )
        ).right()

    private fun Throwable.toFindRouteQueryResponse() = FindRouteQueryResponse(routeFound = false, error = this.message)
}

data class FindRouteQuery(val vehicleId: UUID) : Query {
    override val queryId: UUID = randomUUID()
}

data class FindRouteQueryResponse(
    val routeFound: Boolean,
    val route: RouteQueryResponse? = null,
    val error: String? = null
) {
    data class RouteQueryResponse(
        val cargoId: UUID,
        val cargoSourceStopId: UUID,
        val cargoTargetStopId: UUID,
        val routeTargetStopId: UUID,
        val routeTargetStopDistance: Int
    )
}
