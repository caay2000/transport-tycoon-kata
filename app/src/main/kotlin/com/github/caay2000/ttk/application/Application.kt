package com.github.caay2000.ttk.application

import com.github.caay2000.ttk.api.inbound.Delivery
import com.github.caay2000.ttk.api.inbound.Steps
import com.github.caay2000.ttk.api.inbound.TransportTycoonApi
import com.github.caay2000.ttk.domain.Boat
import com.github.caay2000.ttk.domain.Location
import com.github.caay2000.ttk.domain.Truck
import com.github.caay2000.ttk.domain.World

internal class Application : TransportTycoonApi {

    private val world = World(Truck(Location.FACTORY), Truck(Location.FACTORY), Boat(Location.PORT))

    override fun execute(deliveries: List<Delivery>): Steps {

        val deliveryDestination = deliveries.flatMap {
            when (it) {
                Delivery.valueOf(Location.WAREHOUSE_B.name) -> listOf(Location.WAREHOUSE_B)
                else -> emptyList()
            }
        }

        TODO("Not yet implemented")
    }
}
