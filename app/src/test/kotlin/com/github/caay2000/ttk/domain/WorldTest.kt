package com.github.caay2000.ttk.domain

import com.github.caay2000.ttk.api.inbound.Cargo
import com.github.caay2000.ttk.domain.Location.FACTORY
import com.github.caay2000.ttk.domain.Location.PORT
import com.github.caay2000.ttk.domain.Location.WAREHOUSE_A
import com.github.caay2000.ttk.domain.Location.WAREHOUSE_B
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle

@TestInstance(Lifecycle.PER_CLASS)
class WorldTest {

    @BeforeEach
    fun setUp() {
        Stop.cleanAll()
    }

    @Nested
    inner class AddCargoTests {

        @Test
        internal fun `adds cargo to FACTORY`() {

            val sut = World(Stop.all())
            sut.addCargo(listOf(Cargo(WAREHOUSE_A), Cargo(WAREHOUSE_B), Cargo(WAREHOUSE_A)))

            assertThat(Stop.get(FACTORY).cargo).hasSize(3)
            assertThat(Stop.get(FACTORY).cargo.filter { it.destination == WAREHOUSE_A }).hasSize(2)
            assertThat(Stop.get(FACTORY).cargo.filter { it.destination == WAREHOUSE_B }).hasSize(1)
        }
    }

    @Nested
    inner class IsCompletedTests {

        @Test
        internal fun `is not completed while any stop has cargo`() {

            val sut = World(Stop.all())
            sut.addCargo(listOf(Cargo(WAREHOUSE_B)))

            assertThat(sut.isCompleted()).isFalse
        }

        @Test
        internal fun `is not completed while any vehicle has cargo`() {

            val sut = World(Stop.all())
            sut.createVehicle(VehicleType.TRUCK, FACTORY)
            sut.addCargo(listOf(Cargo(WAREHOUSE_B)))
            sut.update()

            assertThat(sut.isCompleted()).isFalse
        }

        @Test
        internal fun `is completed when all stops and all vehicles are empty`() {

            val sut = World(Stop.all())
            sut.createVehicle(VehicleType.TRUCK, FACTORY)
            sut.addCargo(listOf(Cargo(PORT)))
            sut.update()
            sut.update()

            assertThat(sut.isCompleted()).isTrue
        }
    }

    @Nested
    inner class TimeTests {

        @Test
        internal fun `returns 0 for no updates`() {
            val sut = World(Stop.all())

            assertThat(sut.time).isEqualTo(0)
        }

        @Test
        internal fun `returns x for x updates`() {
            val sut = World(Stop.all())
            sut.addCargo(listOf(Cargo(PORT)))
            sut.update()
            sut.update()

            assertThat(sut.time).isEqualTo(2)
        }
    }

    @Nested
    inner class EventTests {

        @Test
        internal fun `returns no events for empty map`() {
            val sut = World(Stop.all())
            val events = sut.update()

            assertThat(events).hasSize(0)
        }
    }
}
