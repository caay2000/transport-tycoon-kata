package com.github.caay2000.ttk.context.vehicle.configuration.application.find

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.getOrHandle
import arrow.core.handleErrorWith
import arrow.core.right
import com.github.caay2000.ttk.context.shared.domain.VehicleType
import com.github.caay2000.ttk.context.vehicle.configuration.domain.VehicleConfiguration
import com.github.caay2000.ttk.context.vehicle.configuration.domain.VehicleConfigurationRepository
import com.github.caay2000.ttk.lib.eventbus.query.Query
import com.github.caay2000.ttk.lib.eventbus.query.QueryHandler
import java.util.UUID

class FindVehicleConfigurationQueryHandler(
    vehicleConfigurationRepository: VehicleConfigurationRepository
) : QueryHandler<FindVehicleConfigurationQuery, FindVehicleConfigurationQueryResponse> {

    private val vehicleConfigurationFinderService = VehicleConfigurationFinderService(vehicleConfigurationRepository)

    @Suppress("UNREACHABLE_CODE")
    override fun handle(query: FindVehicleConfigurationQuery): FindVehicleConfigurationQueryResponse =
        vehicleConfigurationFinderService.invoke(query.vehicleType)
            .flatMap { configuration -> configuration.toQueryResponse() }
            .handleErrorWith { error -> error.failedQueryResponse() }
            .getOrHandle { throw it }

    private fun VehicleConfiguration.toQueryResponse(): Either<Throwable, FindVehicleConfigurationQueryResponse> =
        FindVehicleConfigurationQueryResponse(
            success = true,
            data = FindVehicleConfigurationQueryResponse.VehicleConfigurationQueryResponse(type, loadTime, speed, capacity)
        ).right()

    private fun Throwable.failedQueryResponse() = FindVehicleConfigurationQueryResponse(success = false, data = null, error = this.message).right()
}

data class FindVehicleConfigurationQuery(val vehicleType: VehicleType) : Query {
    override val queryId: UUID = randomUUID()
}

data class FindVehicleConfigurationQueryResponse(
    val success: Boolean,
    val data: VehicleConfigurationQueryResponse?,
    val error: String? = null
) {
    data class VehicleConfigurationQueryResponse(val type: VehicleType, val loadTime: Int, val speed: Double, val capacity: Int)
}
