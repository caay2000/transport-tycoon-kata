package com.github.caay2000.ttk.application

import com.github.caay2000.ttk.application.configuration.ApplicationConfiguration
import com.github.caay2000.ttk.context.shared.domain.StopId
import com.github.caay2000.ttk.context.shared.domain.VehicleId
import com.github.caay2000.ttk.context.shared.domain.VehicleTypeEnum
import com.github.caay2000.ttk.context.shared.domain.WorldId
import com.github.caay2000.ttk.context.shared.domain.randomDomainId
import com.github.caay2000.ttk.context.vehicle.configuration.domain.VehicleConfiguration
import com.github.caay2000.ttk.context.vehicle.vehicle.primary.http.HttpController.Companion.HTTP_CONFIGURE_VEHICLE
import com.github.caay2000.ttk.context.world.world.application.find.FindWorldQueryResponse
import com.github.caay2000.ttk.context.world.world.primary.http.HttpController.Companion.HTTP_CREATE_CONNECTION
import com.github.caay2000.ttk.context.world.world.primary.http.HttpController.Companion.HTTP_CREATE_STOP
import com.github.caay2000.ttk.context.world.world.primary.http.HttpController.Companion.HTTP_CREATE_VEHICLE
import com.github.caay2000.ttk.context.world.world.primary.http.HttpController.Companion.HTTP_CREATE_WORLD
import com.github.caay2000.ttk.context.world.world.primary.http.HttpController.Companion.HTTP_GET_WORLD
import com.github.caay2000.ttk.context.world.world.primary.http.HttpController.Companion.HTTP_PRODUCE_CARGO
import com.github.caay2000.ttk.context.world.world.primary.http.HttpController.Companion.HTTP_UPDATE_WORLD
import com.github.caay2000.ttk.lib.eventbus.event.Event
import java.util.UUID

class Application(configuration: ApplicationConfiguration) {

    private val httpWorld = configuration.httpWorldController
    private val httpVehicle = configuration.httpVehicleController
    private val dateTimeProvider = configuration.dateTimeProvider

    private val worldId = WorldId()
    private val factoryStopId = StopId()
    private val portStopId = StopId()
    private val warehouseAStopId = StopId()
    private val warehouseBStopId = StopId()

    fun create(configurations: Set<VehicleConfiguration>): ApplicationWorld {

        httpWorld(HTTP_CREATE_WORLD, worldId.uuid)

        httpWorld(HTTP_CREATE_STOP, worldId.uuid, factoryStopId.uuid, "FACTORY")
        httpWorld(HTTP_CREATE_STOP, worldId.uuid, portStopId.uuid, "PORT")
        httpWorld(HTTP_CREATE_STOP, worldId.uuid, warehouseAStopId.uuid, "WAREHOUSE_A")
        httpWorld(HTTP_CREATE_STOP, worldId.uuid, warehouseBStopId.uuid, "WAREHOUSE_B")

        httpWorld(HTTP_CREATE_CONNECTION, worldId.uuid, factoryStopId.uuid, portStopId.uuid, 1, setOf(VehicleTypeEnum.TRUCK.name))
        httpWorld(HTTP_CREATE_CONNECTION, worldId.uuid, portStopId.uuid, warehouseAStopId.uuid, 4, setOf(VehicleTypeEnum.BOAT.name))
        httpWorld(HTTP_CREATE_CONNECTION, worldId.uuid, factoryStopId.uuid, warehouseBStopId.uuid, 5, setOf(VehicleTypeEnum.TRUCK.name))

        httpVehicle(HTTP_CONFIGURE_VEHICLE, configurations)

        httpWorld(HTTP_CREATE_VEHICLE, worldId.uuid, factoryStopId.uuid, randomVehicleId(), VehicleTypeEnum.TRUCK.name)
        httpWorld(HTTP_CREATE_VEHICLE, worldId.uuid, factoryStopId.uuid, randomVehicleId(), VehicleTypeEnum.TRUCK.name)
        httpWorld(HTTP_CREATE_VEHICLE, worldId.uuid, portStopId.uuid, randomVehicleId(), VehicleTypeEnum.BOAT.name)

        val world = httpWorld(HTTP_GET_WORLD, worldId.uuid).body as FindWorldQueryResponse

        return world.toApplicationWorld()
    }

    fun execute(cargoDestinations: List<String>): Result {

        cargoDestinations.forEach { destination ->
            httpWorld(HTTP_PRODUCE_CARGO, worldId.uuid, factoryStopId.uuid, UUID.randomUUID(), findId(destination))
        }

        httpWorld(HTTP_UPDATE_WORLD, worldId.uuid)

        return Result(
            duration = dateTimeProvider.now().value(),
            events = emptyList()
        )
    }

    private fun findId(name: String): UUID =
        when (name) {
            "FACTORY" -> factoryStopId.uuid
            "PORT" -> portStopId.uuid
            "WAREHOUSE_A" -> warehouseAStopId.uuid
            "WAREHOUSE_B" -> warehouseBStopId.uuid
            else -> throw RuntimeException("")
        }

    private fun FindWorldQueryResponse.toApplicationWorld(): ApplicationWorld {
        if (this.success.not()) throw RuntimeException("ERROR")
        return ApplicationWorld(
            stops = this.data!!.stops.map { stop ->
                ApplicationStop(
                    name = stop.name,
                    connections = stop.connections.map { connection ->
                        ApplicationConnection(
                            targetStop = this.findStopName(connection.targetStopId),
                            distance = connection.distance,
                            allowedVehicleTypes = connection.allowedVehicleTypes
                        )
                    }
                )
            },
            vehicles = this.data!!.vehicles.map { vehicle ->
                ApplicationVehicle(vehicle.type)
            }
        )
    }

    private fun FindWorldQueryResponse.findStopName(stopId: UUID): String =
        this.data!!.stops.first { it.id == stopId }.name

    private fun randomVehicleId(): UUID = randomDomainId<VehicleId>().uuid

    class Result(
        val duration: Int,
        val events: List<Event>
    )
}
