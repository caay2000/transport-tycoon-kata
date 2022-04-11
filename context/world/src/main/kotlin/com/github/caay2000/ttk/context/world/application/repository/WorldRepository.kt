package com.github.caay2000.ttk.context.world.application.repository

import com.github.caay2000.ttk.context.shared.domain.WorldId
import com.github.caay2000.ttk.context.world.domain.World

interface WorldRepository {

    fun save(world: World): World
    fun get(id: WorldId): World
}
