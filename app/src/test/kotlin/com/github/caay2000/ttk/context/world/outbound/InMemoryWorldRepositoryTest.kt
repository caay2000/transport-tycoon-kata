package com.github.caay2000.ttk.context.world.outbound

import com.github.caay2000.ttk.context.core.domain.WorldId
import com.github.caay2000.ttk.context.world.domain.World
import com.github.caay2000.ttk.lib.database.InMemoryDatabase
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class InMemoryWorldRepositoryTest {

    private val sut = InMemoryWorldRepository(InMemoryDatabase())

    @Test
    fun `world is saved and retrieved correctly`() {

        val id = WorldId()
        val world = World(id, emptyList())

        sut.save(world)

        assertThat(sut.get(id)).isEqualTo(world)
    }
}
