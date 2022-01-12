package com.github.caay2000.ttk.integration

import com.github.caay2000.ttk.App
import com.github.caay2000.ttk.api.inbound.ArriveEvent
import com.github.caay2000.ttk.api.inbound.Cargo
import com.github.caay2000.ttk.api.inbound.DepartEvent
import com.github.caay2000.ttk.api.inbound.Event
import com.github.caay2000.ttk.domain.Location
import com.github.caay2000.ttk.domain.VehicleType
import org.assertj.core.api.AbstractAssert
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.MethodSource
import java.util.UUID
import java.util.stream.Stream

@TestInstance(PER_CLASS)
class AppIntegrationTest {

    @CsvSource(
        "B, 5",
        "A, 5",
        "AB, 5",
        "BB, 5",
        "BBB, 15",
        "ABB, 7",
        "AABABBAB, 29",
        "ABBBABAAABBB, 41"
    )
    @ParameterizedTest(name = "{index} - {0} route takes {1} steps")
    fun `route takes the correct steps`(deliveries: String, steps: Int) {
        val result = App().invoke(deliveries)
        assertThat(result.duration).isEqualTo(steps)
    }

    @MethodSource("routesAndEvents")
    @ParameterizedTest(name = "{index} - {0} route returns correct events")
    fun `route returns the correct events`(deliveries: String, events: List<Event>) {
        val result = App().invoke(deliveries)
        EventAssert.assertThat(result.events[0]).isEqualTo(
            departEvent(
                time = 0,
                location = Location.FACTORY,
                destination = Location.WAREHOUSE_B,
                cargoDestination = Location.WAREHOUSE_B
            )
        )
    }

    private fun routesAndEvents(): Stream<Arguments> {
        return Stream.of(
            Arguments.of(
                "B",
                listOf(
                    departEvent(
                        time = 0,
                        location = Location.FACTORY,
                        destination = Location.WAREHOUSE_B,
                        cargoDestination = Location.WAREHOUSE_B
                    ),
                    arriveEvent(
                        time = 5,
                        location = Location.WAREHOUSE_B,
                        cargoDestination = Location.WAREHOUSE_B
                    )
                )
            )
        )
    }

    private fun departEvent(
        time: Int,
        location: Location,
        destination: Location,
        cargoDestination: Location
    ) = DepartEvent(
        time = time,
        vehicleId = UUID.randomUUID(),
        type = VehicleType.TRUCK,
        location = location,
        destination = destination,
        cargo = Cargo(cargoDestination)
    )

    private fun arriveEvent(
        time: Int,
        location: Location,
        cargoDestination: Location
    ) = ArriveEvent(
        time = time,
        vehicleId = UUID.randomUUID(),
        type = VehicleType.TRUCK,
        location = location,
        cargo = Cargo(cargoDestination)
    )
}

class EventAssert(actual: Event) : AbstractAssert<EventAssert, Event>(actual, EventAssert::class.java) {

    override fun isEqualTo(expected: Any?): EventAssert {

        assertThat(expected).isInstanceOf(Event::class.java)
        return assertThat(actual).isInstanceOfSatisfying(Event::class.java) {
            assertThat(actual.time).isEqualTo(it.time)
            when (it) {
                is DepartEvent -> {
                    assertThat(expected).isInstanceOfSatisfying(DepartEvent::class.java) { exp ->
                        assertThat(it.type).isEqualTo(exp.type)
                        assertThat(it.location).isEqualTo(exp.location)
                        assertThat(it.destination).isEqualTo(exp.destination)
                    }
                }
                is ArriveEvent -> {
                    assertThat(expected).isInstanceOfSatisfying(ArriveEvent::class.java) { exp ->
                        assertThat(it.type).isEqualTo(exp.type)
                        assertThat(it.location).isEqualTo(exp.location)
                    }
                }
            }
        }
    }

    companion object {
        fun assertThat(actual: Event) = EventAssert(actual)
    }
}
