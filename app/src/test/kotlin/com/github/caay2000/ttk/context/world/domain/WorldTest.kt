//package com.github.caay2000.ttk.context.world.domain
//
//import com.github.caay2000.ttk.context.core.domain.WorldId
//import com.github.caay2000.ttk.context.time.domain.DateTime
//import com.github.caay2000.ttk.context.vehicle.domain.VehicleType
//import com.github.caay2000.ttk.context.world.domain.Location.FACTORY
//import com.github.caay2000.ttk.context.world.domain.Location.PORT
//import com.github.caay2000.ttk.context.world.domain.Location.WAREHOUSE_A
//import com.github.caay2000.ttk.context.world.domain.Location.WAREHOUSE_B
//import org.assertj.core.api.Assertions.assertThat
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Nested
//import org.junit.jupiter.api.Test
//import org.junit.jupiter.api.TestInstance
//import org.junit.jupiter.api.TestInstance.Lifecycle
//
//@TestInstance(Lifecycle.PER_CLASS)
//class WorldTest {
//
//    @BeforeEach
//    fun setUp() {
//        Stop.cleanAll()
//    }
//
//    @Nested
//    inner class AddCargoTests {
//
//        @Test
//        internal fun `adds cargo to FACTORY`() {
//
//            val sut = World(WorldId(), Stop.all().toSet())
//            listOf(WAREHOUSE_A, WAREHOUSE_B, WAREHOUSE_A).forEach {
//                sut.addCargo(Cargo(destination = it))
//            }
//
//            assertThat(Stop.get(FACTORY).cargo).hasSize(3)
//            assertThat(Stop.get(FACTORY).cargo.filter { it.destination == WAREHOUSE_A }).hasSize(2)
//            assertThat(Stop.get(FACTORY).cargo.filter { it.destination == WAREHOUSE_B }).hasSize(1)
//        }
//    }
//
//    @Nested
//    inner class IsCompletedTests {
//
//        @Test
//        internal fun `is not completed while any stop has cargo`() {
//
//            val sut = World(WorldId(), Stop.all().toSet())
//            sut.addCargo(Cargo(destination = WAREHOUSE_B))
//
//            assertThat(sut.isCompleted()).isFalse
//        }
//
//        @Test
//        internal fun `is not completed while any vehicle has cargo`() {
//
//            val sut = World(WorldId(), Stop.all().toSet())
//            sut.createVehicle(VehicleType.TRUCK, FACTORY)
//            sut.addCargo(Cargo(destination = WAREHOUSE_B))
//            sut.update(dateTime)
//
//            assertThat(sut.isCompleted()).isFalse
//        }
//
//        @Test
//        internal fun `is completed when all stops and all vehicles are empty`() {
//
//            val sut = World(WorldId(), Stop.all().toSet())
//            sut.createVehicle(VehicleType.TRUCK, FACTORY)
//            sut.addCargo(Cargo(destination = PORT))
//            sut.update(dateTime)
//            sut.update(dateTime)
//
//            assertThat(sut.isCompleted()).isTrue
//        }
//    }
//
//    private companion object {
//        private val dateTime = DateTime(0)
//    }
//}
