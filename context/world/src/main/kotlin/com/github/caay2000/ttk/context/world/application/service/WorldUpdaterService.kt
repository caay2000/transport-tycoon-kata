package com.github.caay2000.ttk.context.world.application.service

import com.github.caay2000.ttk.context.shared.domain.WorldId
import com.github.caay2000.ttk.context.world.application.repository.WorldRepository
import com.github.caay2000.ttk.context.world.domain.World
import com.github.caay2000.ttk.lib.eventbus.event.Event
import com.github.caay2000.ttk.lib.eventbus.event.EventPublisher

class WorldUpdaterService(
    private val eventPublisher: EventPublisher<Event>,
    private val worldRepository: WorldRepository
) {

    fun invoke(worldId: WorldId) {

        val world = worldRepository.get(worldId)
        world.update()
        world.save()

        eventPublisher.publish(world.pullEvents())
    }

    private fun World.save() = worldRepository.save(this)
}
