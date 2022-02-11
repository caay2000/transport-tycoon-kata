package com.github.caay2000.ttk.context.world.domain.service

import com.github.caay2000.ttk.context.core.domain.DateTimeProvider
import com.github.caay2000.ttk.context.core.domain.WorldId
import com.github.caay2000.ttk.context.core.event.EventPublisher
import com.github.caay2000.ttk.context.world.domain.World
import com.github.caay2000.ttk.context.world.domain.repository.WorldRepository

class WorldUpdaterService(
    private val eventPublisher: EventPublisher,
    private val worldRepository: WorldRepository,
    private val dateTimeProvider: DateTimeProvider
) {

    fun invoke(worldId: WorldId) {

        val world = worldRepository.get(worldId)
        world.update(dateTimeProvider.now())
        world.save()

        eventPublisher.publish(world.pullEvents())
    }

    private fun World.save() = worldRepository.save(this)
}
