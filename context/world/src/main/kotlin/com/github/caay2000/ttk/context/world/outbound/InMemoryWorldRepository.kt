package com.github.caay2000.ttk.context.world.outbound

import arrow.core.Either
import arrow.core.Option
import com.github.caay2000.ttk.context.shared.domain.WorldId
import com.github.caay2000.ttk.context.world.domain.World
import com.github.caay2000.ttk.context.world.domain.repository.StopRepository
import com.github.caay2000.ttk.context.world.domain.repository.WorldRepository
import com.github.caay2000.ttk.lib.database.InMemoryDatabase

class InMemoryWorldRepository(private val database: InMemoryDatabase) : WorldRepository {

    private val stopRepository: StopRepository = InMemoryStopRepository(database)

    override fun save(world: World) = Either.catch { database.save(DATABASE_TABLE, world.id.rawId, world) as World }
    override fun get(id: WorldId) =
        Option.catch {
            val world = database.getById(DATABASE_TABLE, id.rawId) as World
            val stops = stopRepository.findAllByWorldId(id)
            world.copy(stops = stops)
        }

    override fun exists(id: WorldId): Boolean = database.exists(DATABASE_TABLE, id.rawId)

    companion object {
        private const val DATABASE_TABLE = "WORLD_CTX_world"
    }
}
