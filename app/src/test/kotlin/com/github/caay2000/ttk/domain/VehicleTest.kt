// package com.github.caay2000.ttk.domain
//
// import com.github.caay2000.ttk.domain.event.Arrived
// import com.github.caay2000.ttk.domain.event.Departed
// import org.assertj.core.api.Assertions.assertThat
// import org.junit.jupiter.api.Nested
// import org.junit.jupiter.api.Test
// import java.util.UUID
//
// internal class VehicleTest {
//
//    @Nested
//    inner class ConstructorTests {
//
//        @Test
//        fun `starts with a location`() {
//
//            val sut = Truck(Location.FACTORY)
//            assertThat(sut.stop).isEqualTo(Location.FACTORY)
//        }
//
//        @Test
//        fun `starts with an id`() {
//
//            val sut = Truck(Location.FACTORY)
//            assertThat(sut.id)
//                .isNotNull
//                .isInstanceOf(UUID::class.java)
//        }
//
//        @Test
//        fun `starts with STOP status`() {
//
//            val sut = Truck(Location.FACTORY)
//            assertThat(sut.status).isEqualTo(VehicleStatus.STOP)
//        }
//    }
//
//    @Nested
//    inner class RouteTests {
//
//        @Test
//        fun `starts with a null route`() {
//
//            val sut = Truck(Location.FACTORY)
//            assertThat(sut.route).isNull()
//            assertThat(sut.distanceToNextStop).isEqualTo(0)
//        }
//
//        @Test
//        fun `assign route changes route and status to IDLE`() {
//
//            val sut = Truck(Location.FACTORY)
//            assertThat(sut.route).isNull()
//
//            val route = Route(Location.FACTORY, Location.WAREHOUSE_B)
//            sut.assignRoute(route)
//            assertThat(sut.route).isEqualTo(route)
//            assertThat(sut.status).isEqualTo(VehicleStatus.IDLE)
//            assertThat(sut.distanceToNextStop).isEqualTo(5)
//        }
//    }
//
//    @Nested
//    inner class UpdateTests {
//        @Test
//        fun `vehicle without route maintains location and status`() {
//
//            val sut = Truck(Location.FACTORY)
//
//            sut.update(0)
//            assertThat(sut.route).isEqualTo(null)
//            assertThat(sut.stop).isEqualTo(Location.FACTORY)
//            assertThat(sut.status).isEqualTo(VehicleStatus.STOP)
//        }
//
//        @Test
//        fun `vehicle starting route moves towards destination`() {
//
//            val sut = Truck(Location.FACTORY)
//            sut.assignRoute(Route(Location.FACTORY, Location.WAREHOUSE_B))
//
//            sut.update(0)
//            assertThat(sut.stop).isEqualTo(Location.FACTORY)
//            assertThat(sut.status).isEqualTo(VehicleStatus.ON_ROUTE)
//            assertThat(sut.distanceToNextStop).isEqualTo(5)
//        }
//
//        @Test
//        fun `vehicle reaching destination stops`() {
//
//            val sut = Truck(Location.FACTORY)
//            sut.assignRoute(Route(Location.FACTORY, Location.PORT))
//
//            sut.update(0)
//            sut.update(1)
//            assertThat(sut.stop).isEqualTo(Location.PORT)
//            assertThat(sut.status).isEqualTo(VehicleStatus.STOP)
//            assertThat(sut.distanceToNextStop).isEqualTo(0)
//        }
//
//        @Test
//        fun `vehicle reaching destination continues to next station`() {
//
//            val sut = Truck(Location.FACTORY)
//            sut.assignRoute(Route(Location.FACTORY, Location.PORT, Location.FACTORY))
//
//            sut.update(0)
//            sut.update(1)
//            sut.update(2)
//            assertThat(sut.stop).isEqualTo(Location.FACTORY)
//        }
//
//        @Test
//        fun `vehicle moves from one station to next one`() {
//
//            val sut = Truck(Location.FACTORY)
//            sut.assignRoute(Route(Location.FACTORY, Location.WAREHOUSE_B, Location.FACTORY))
//            var time = 0
//            repeat(sut.distanceToNextStop) { sut.update(time++) }
//            sut.update(time++)
//            assertThat(sut.status).isEqualTo(VehicleStatus.ON_ROUTE)
//            assertThat(sut.distanceToNextStop).isEqualTo(5)
//        }
//    }
//
//    @Nested
//    inner class VehicleEventTests {
//        @Test
//        fun `creates Depart event when starting a route`() {
//            val sut = Truck(Location.FACTORY)
//            sut.assignRoute(Route(Location.FACTORY, Location.WAREHOUSE_B))
//            val events = sut.update(0)
//            assertThat(events).isEqualTo(
//                listOf(Departed(0, Location.FACTORY, sut))
//            )
//        }
//
//        @Test
//        fun `creates Arrive event when reaching stop`() {
//            val sut = Truck(Location.FACTORY)
//            sut.assignRoute(Route(Location.FACTORY, Location.WAREHOUSE_B))
//            var time = 0
//            repeat(sut.distanceToNextStop) {
//                sut.update(time++)
//            }
//            val events = sut.update(time++)
//            assertThat(events).isEqualTo(
//                listOf(Arrived(5, Location.WAREHOUSE_B, sut))
//            )
//        }
//
//        @Test
//        fun `creates Arrive and depart event when reaching stop and continuing to next stop`() {
//            val sut = Truck(Location.FACTORY)
//            sut.assignRoute(Route(Location.FACTORY, Location.PORT, Location.FACTORY))
//            sut.update(0)
//            val events = sut.update(1)
//            assertThat(events).isEqualTo(
//                listOf(
//                    Arrived(1, Location.PORT, sut),
//                    Departed(1, Location.PORT, sut)
//                )
//            )
//        }
//    }
// }
