package com.github.caay2000.ttk.context.world.application.service

import com.github.caay2000.ttk.context.core.domain.WorldId
import com.github.caay2000.ttk.context.core.event.EventPublisher
import com.github.caay2000.ttk.context.world.application.repository.WorldRepository
import com.github.caay2000.ttk.context.world.domain.World

class WorldUpdaterService(
    private val eventPublisher: EventPublisher,
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
