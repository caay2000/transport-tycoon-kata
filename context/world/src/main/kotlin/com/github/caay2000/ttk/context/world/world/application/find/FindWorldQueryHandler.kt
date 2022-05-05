package com.github.caay2000.ttk.context.world.world.application.find

import arrow.core.flatMap
import arrow.core.getOrHandle
import arrow.core.right
import com.github.caay2000.ttk.context.shared.domain.toDomainId
import com.github.caay2000.ttk.context.world.stop.domain.Stop
import com.github.caay2000.ttk.context.world.vehicle.domain.Vehicle
import com.github.caay2000.ttk.context.world.world.domain.World
import com.github.caay2000.ttk.context.world.world.domain.WorldRepository
import com.github.caay2000.ttk.lib.eventbus.query.Query
import com.github.caay2000.ttk.lib.eventbus.query.QueryHandler
import java.util.UUID

class FindWorldQueryHandler(
    worldRepository: WorldRepository
) : QueryHandler<FindWorldQuery, FindWorldQueryResponse> {

    private val worldFinderService = WorldFinderService(worldRepository)

    override fun handle(query: FindWorldQuery): FindWorldQueryResponse =
        worldFinderService.invoke(query.worldId.toDomainId())
            .flatMap { world -> world.toQueryResponse().right() }
            .getOrHandle { throw it }

    private fun World.toQueryResponse(): FindWorldQueryResponse =
        FindWorldQueryResponse(
            success = true,
            data = FindWorldQueryResponse.WorldQueryResponse(
                worldId = this.id.uuid,
                stops = this.stops.mapStopResponse(),
                vehicles = this.vehicles.mapVehicleResponse()
            )
        )

    private fun Set<Stop>.mapStopResponse(): Set<FindWorldQueryResponse.StopQueryResponse> = map { stop ->
        FindWorldQueryResponse.StopQueryResponse(
            id = stop.id.uuid,
            name = stop.name,
            connections = stop.connections.map { connection ->
                FindWorldQueryResponse.ConnectionQueryResponse(
                    sourceStopId = connection.sourceStopId.uuid,
                    targetStopId = connection.targetStopId.uuid,
                    distance = connection.distance,
                    allowedVehicleTypes = connection.allowedVehicleTypes.map { it.name }
                        .toSet(),
                )
            }.toSet(),
        )
    }.toSet()

    private fun Set<Vehicle>.mapVehicleResponse(): Set<FindWorldQueryResponse.VehicleQueryResponse> = map { vehicle ->
        FindWorldQueryResponse.VehicleQueryResponse(
            id = vehicle.id.uuid,
            type = vehicle.type.name
        )
    }.toSet()
}

data class FindWorldQuery(val worldId: UUID) : Query {
    override val queryId: UUID = randomUUID()
}

data class FindWorldQueryResponse(
    val success: Boolean,
    val data: WorldQueryResponse?,
    val error: String? = null
) {

    data class WorldQueryResponse(
        val worldId: UUID,
        val stops: Set<StopQueryResponse>,
        val vehicles: Set<VehicleQueryResponse>
    )

    data class StopQueryResponse(
        val id: UUID,
        val name: String,
        val connections: Set<ConnectionQueryResponse>
    )

    data class ConnectionQueryResponse(
        val sourceStopId: UUID,
        val targetStopId: UUID,
        val distance: Int,
        val allowedVehicleTypes: Set<String>
    )

    data class VehicleQueryResponse(
        val id: UUID,
        val type: String
    )
}
