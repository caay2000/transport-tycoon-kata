package com.github.caay2000.ttk.context.world.outbound

import com.github.caay2000.ttk.context.shared.domain.WorldId
import com.github.caay2000.ttk.context.world.application.repository.WorldRepository
import com.github.caay2000.ttk.context.world.domain.World
import com.github.caay2000.ttk.lib.database.InMemoryDatabase

class InMemoryWorldRepository(private val database: InMemoryDatabase) : WorldRepository {

    // TODO create a DTO object for world

    override fun save(world: World) = database.save(DATABASE_TABLE, world.id.rawId, world) as World
    override fun get(id: WorldId) = database.getById(DATABASE_TABLE, id.rawId) as World

    companion object {
        private const val DATABASE_TABLE = "world"
    }
}
