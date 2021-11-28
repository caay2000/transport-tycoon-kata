package com.github.caay2000.ttk.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class RouteTest {

    @Test
    fun `route is created correctly`() {
        val sut = Route(Location.FACTORY, Location.PORT, Location.FACTORY)

        assertThat(sut.stops).hasSize(3)
        assertThat(sut.numStops).isEqualTo(3)
    }

    @Test
    fun `returns correct total distance when just 1 stop`() {
        val sut = Route(Location.FACTORY)

        assertThat(sut.totalRouteDistance).isEqualTo(0)
    }

    @Test
    fun `returns correct total distance for multiple stops`() {
        val sut = Route(Location.FACTORY, Location.PORT, Location.FACTORY)

        assertThat(sut.totalRouteDistance).isEqualTo(2)
    }

    @Test
    fun `nextStop returns the next stop when there are more stop`() {
        val sut = Route(Location.FACTORY, Location.PORT, Location.FACTORY)

        assertThat(sut.nextStop(sut.stops[0])).isEqualTo(sut.stops[1])
    }

    @Test
    fun `nextStop returns the current stop when its the last stop`() {
        val sut = Route(Location.FACTORY, Location.PORT, Location.FACTORY)

        assertThat(sut.nextStop(sut.stops[2])).isEqualTo(sut.stops[2])
    }
}
