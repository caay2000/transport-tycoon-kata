// package com.github.caay2000.ttk.domain
//
// import org.assertj.core.api.Assertions.assertThat
// import org.junit.jupiter.api.Test
//
// internal class WorldTest {
//
//    @Test
//    fun `initialised correctly`() {
//
//        val vehicle = Truck(Location.FACTORY)
//
//        val sut = World(vehicle)
//
//        assertThat(sut.vehicles).hasSize(1)
//            .isEqualTo(listOf(vehicle))
//    }
//
//    @Test
//    fun `deploys multiple vehicles correctly`() {
//
//        val sut = World(Truck(Location.FACTORY), Boat(Location.PORT))
//        assertThat(sut.vehicles).hasSize(2)
//    }
// }
