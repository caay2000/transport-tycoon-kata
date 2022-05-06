package com.github.caay2000.ttk

import com.github.caay2000.ttk.application.Application
import com.github.caay2000.ttk.application.configuration.ApplicationConfiguration
import com.github.caay2000.ttk.context.shared.domain.VehicleType
import com.github.caay2000.ttk.context.vehicle.configuration.domain.VehicleConfiguration

class App {

    private val application = Application(ApplicationConfiguration())
    private val adapter = LocationAdapter()

    private val defaultConfiguration = setOf(
        VehicleConfiguration.create(VehicleType.TRUCK, 0, 1.0, 1),
        VehicleConfiguration.create(VehicleType.BOAT, 0, 1.0, 1)
    )

    fun invoke(input: String, vehicleConfiguration: Set<VehicleConfiguration> = defaultConfiguration): Application.Result {
        application.create(vehicleConfiguration)
        return application.execute(adapter.execute(input))
    }

    private class LocationAdapter {

        fun execute(input: String) = parseRoute(input)

        private fun parseRoute(input: String): List<String> {
            val regex = "^([AB])+\$".toRegex()
            val values = regex.find(input)!!.value
            return values.asIterable()
                .filterNot { it.isWhitespace() }
                .map { it.toLocation() }
        }

        private fun Char.toLocation(): String =
            when (this) {
                'A' -> "WAREHOUSE_A"
                'B' -> "WAREHOUSE_B"
                else -> throw IllegalArgumentException("Invalid input")
            }
    }
}

fun main(args: Array<String>) {
    println(App().invoke(input = args[0]).duration)
}
