package com.github.caay2000.ttk.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class LocationTest {

    @CsvSource(
        "FACTORY, PORT, 1",
        "PORT, WAREHOUSE_A, 4",
        "FACTORY, WAREHOUSE_B, 5",
        "PORT, FACTORY, 1",
        "WAREHOUSE_A, PORT, 4",
        "WAREHOUSE_B, FACTORY, 5"
    )
    @ParameterizedTest(name = "{index} - distance from {0} to {1} is {2}")
    internal fun `distanceTo works`(src: Location, dst: Location, distance: Distance) {

        val result = src.distanceTo(dst)
        assertThat(result).isEqualTo(distance)
    }

    @CsvSource(
        "FACTORY, FACTORY",
        "PORT, PORT",
        "WAREHOUSE_A, WAREHOUSE_A",
        "WAREHOUSE_B, WAREHOUSE_B"
    )
    @ParameterizedTest(name = "{index} - distance from {0} to {1} is 0")
    internal fun `same location paths has 0 distance`(src: Location, dst: Location) {

        val result = src.distanceTo(dst)
        assertThat(result).isEqualTo(0)
    }

    @CsvSource(
        "FACTORY, WAREHOUSE_A",
        "PORT, WAREHOUSE_B",
        "WAREHOUSE_A, WAREHOUSE_B"
    )
    @ParameterizedTest(name = "{index} - distance from {0} to {1} is invalid")
    internal fun `invalid paths has -1 distance`(src: Location, dst: Location) {

        val result = src.distanceTo(dst)
        assertThat(result).isEqualTo(-1)
    }
}
