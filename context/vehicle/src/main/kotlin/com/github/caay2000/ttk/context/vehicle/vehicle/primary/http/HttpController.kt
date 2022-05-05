package com.github.caay2000.ttk.context.vehicle.vehicle.primary.http

import com.github.caay2000.ttk.context.vehicle.configuration.application.create.CreateVehicleConfigurationCommand
import com.github.caay2000.ttk.context.vehicle.configuration.domain.VehicleConfiguration
import com.github.caay2000.ttk.lib.eventbus.command.Command
import com.github.caay2000.ttk.lib.eventbus.command.CommandBus
import com.github.caay2000.ttk.lib.http.HttpMethod
import com.github.caay2000.ttk.lib.http.HttpRequest
import com.github.caay2000.ttk.lib.http.HttpResult

class HttpController(val commandBus: CommandBus<Command>) {

    companion object {
        val HTTP_CONFIGURE_VEHICLE = HttpRequest(HttpMethod.POST, "/vehicle/{type}/configuration")
    }

    operator fun invoke(method: HttpMethod, url: String, vararg params: Any): HttpResult =
        invoke(HttpRequest(method, url), *params)

    operator fun invoke(httpRequest: HttpRequest, vararg params: Any): HttpResult =
        try {
            when (httpRequest) {
                HTTP_CONFIGURE_VEHICLE -> postConfigureVehicle(params)
                else -> HttpResult.notFound()
            }
        } catch (t: Throwable) {
            println("ERROR: ${t.message}")
            HttpResult.ok(t.message)
        }

    private fun postConfigureVehicle(params: Array<out Any>): HttpResult =
        (params[0] as Set<VehicleConfiguration>).let { config ->
            config.forEach {
                commandBus.publish(CreateVehicleConfigurationCommand(it.type, it.loadTime, it.speed, it.capacity))
            }
        }.let { HttpResult.ok(it) }
}
