package com.github.caay2000.ttk.context.world.application.service

import com.github.caay2000.ttk.context.shared.domain.WorldId
import com.github.caay2000.ttk.context.world.application.repository.WorldRepository
import com.github.caay2000.ttk.context.world.domain.Cargo
import com.github.caay2000.ttk.context.world.domain.Stop
import com.github.caay2000.ttk.context.world.domain.World
import com.github.caay2000.ttk.lib.event.CargoAddedEvent
import com.github.caay2000.ttk.lib.eventbus.event.Event
import com.github.caay2000.ttk.lib.eventbus.event.EventPublisher

class WorldConfiguratorService(
    private val eventPublisher: EventPublisher<Event>,
    private val worldRepository: WorldRepository
) {

    fun invoke(worldId: WorldId): World {

        val world = World.create(id = worldId, stops = Stop.all().toSet())

        return world.save()
            .also { eventPublisher.publish(it.pullEvents()) }
    }

    fun addCargo(worldId: WorldId, cargo: Cargo): World {

        val world = worldRepository.get(worldId)
        world.addCargo(cargo)
        eventPublisher.publish(CargoAddedEvent(world.id.uuid, cargo.id, world.getStop(cargo.origin).id.uuid, world.getStop(cargo.destination).id.uuid))
        return world.save()
    }

    private fun World.save() = worldRepository.save(this)
}
