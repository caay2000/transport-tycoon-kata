package com.github.caay2000.ttk.context.world.world.primary.http

import com.github.caay2000.ttk.context.world.cargo.application.produce.ProduceCargoCommand
import com.github.caay2000.ttk.context.world.stop.application.connection.create.CreateConnectionCommand
import com.github.caay2000.ttk.context.world.stop.application.create.CreateStopCommand
import com.github.caay2000.ttk.context.world.vehicle.application.create.CreateVehicleCommand
import com.github.caay2000.ttk.context.world.world.application.create.CreateWorldCommand
import com.github.caay2000.ttk.context.world.world.application.find.FindWorldQuery
import com.github.caay2000.ttk.context.world.world.application.find.FindWorldQueryResponse
import com.github.caay2000.ttk.context.world.world.application.update.UpdateWorldCommand
import com.github.caay2000.ttk.lib.eventbus.command.Command
import com.github.caay2000.ttk.lib.eventbus.command.CommandBus
import com.github.caay2000.ttk.lib.eventbus.query.QueryBus
import com.github.caay2000.ttk.lib.http.HttpMethod
import com.github.caay2000.ttk.lib.http.HttpRequest
import com.github.caay2000.ttk.lib.http.HttpResult
import java.util.UUID

class HttpController(val commandBus: CommandBus<Command>, val queryBus: QueryBus) {

    companion object {
        val HTTP_GET_WORLD = HttpRequest(HttpMethod.GET, "/world/{worldId}")
        val HTTP_CREATE_WORLD = HttpRequest(HttpMethod.POST, "/world/{worldId}")
        val HTTP_CREATE_STOP = HttpRequest(HttpMethod.POST, "/world/{worldId}/stop/{stopId}")
        val HTTP_CREATE_CONNECTION = HttpRequest(HttpMethod.POST, "/world/{worldId}/stop/{sourceStopId}/connection/{targetStopId}")
        val HTTP_PRODUCE_CARGO = HttpRequest(HttpMethod.POST, "/world/{worldId}/stop/{stopId}/cargo/{cargoId}")
        val HTTP_UPDATE_WORLD = HttpRequest(HttpMethod.POST, "/world/{worldId}/update")

        val HTTP_CREATE_VEHICLE = HttpRequest(HttpMethod.POST, "/vehicle/{vehicleId}")
    }

    operator fun invoke(method: HttpMethod, url: String, vararg params: Any): HttpResult =
        invoke(HttpRequest(method, url), *params)

    operator fun invoke(httpRequest: HttpRequest, vararg params: Any): HttpResult =
        try {
            when (httpRequest) {
                HTTP_GET_WORLD -> getWorld(params)
                HTTP_CREATE_WORLD -> postCreateWorld(params)
                HTTP_CREATE_STOP -> postCreateStop(params)
                HTTP_CREATE_CONNECTION -> postCreateConnection(params)
                HTTP_PRODUCE_CARGO -> postProduceCargo(params)
                HTTP_CREATE_VEHICLE -> postCreateVehicle(params)
                HTTP_UPDATE_WORLD -> postUpdateWorld(params)
                else -> HttpResult.notFound()
            }
        } catch (t: Throwable) {
            println("ERROR: ${t.message}")
            HttpResult.ok(t.message)
        }

    private fun getWorld(params: Array<out Any>): HttpResult =
        queryBus.execute<FindWorldQuery, FindWorldQueryResponse>(FindWorldQuery(params[0] as UUID)).let {
            HttpResult.ok(it)
        }

    private fun postCreateWorld(params: Array<out Any>): HttpResult =
        commandBus.publish(CreateWorldCommand(worldId = params[0] as UUID))
            .let { HttpResult.ok() }

    private fun postCreateStop(params: Array<out Any>): HttpResult =
        commandBus.publish(
            CreateStopCommand(
                worldId = params[0] as UUID,
                stopId = params[1] as UUID,
                stopName = params[2] as String
            )
        ).let { HttpResult.ok() }

    private fun postCreateConnection(params: Array<out Any>): HttpResult =
        commandBus.publish(
            CreateConnectionCommand(
                worldId = params[0] as UUID,
                sourceStopId = params[1] as UUID,
                targetStopId = params[2] as UUID,
                distance = params[3] as Int,
                allowedVehicleTypes = params[4] as Set<String>
            )
        ).let { HttpResult.ok() }

    private fun postProduceCargo(params: Array<out Any>): HttpResult =
        commandBus.publish(
            ProduceCargoCommand(
                worldId = params[0] as UUID,
                stopId = params[1] as UUID,
                cargoId = params[2] as UUID,
                targetStopId = params[3] as UUID
            )
        ).let { HttpResult.ok() }

    private fun postCreateVehicle(params: Array<out Any>): HttpResult =
        commandBus.publish(
            CreateVehicleCommand(
                worldId = params[0] as UUID,
                stopId = params[1] as UUID,
                vehicleId = params[2] as UUID,
                vehicleType = params[3] as String
            )
        ).let { HttpResult.ok() }

    private fun postUpdateWorld(params: Array<out Any>): HttpResult =
        commandBus.publish(UpdateWorldCommand(worldId = params[0] as UUID)).let { HttpResult.ok() }
}
