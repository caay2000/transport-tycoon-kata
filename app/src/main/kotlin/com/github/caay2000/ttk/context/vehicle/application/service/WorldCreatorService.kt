package com.github.caay2000.ttk.context.vehicle.application.service

import com.github.caay2000.ttk.context.core.domain.WorldId
import com.github.caay2000.ttk.context.vehicle.application.repository.WorldRepository
import com.github.caay2000.ttk.context.vehicle.domain.World

class WorldCreatorService(private val worldRepository: WorldRepository) {

    fun invoke(worldId: WorldId) = worldRepository.save(World(worldId, emptySet()))

}
