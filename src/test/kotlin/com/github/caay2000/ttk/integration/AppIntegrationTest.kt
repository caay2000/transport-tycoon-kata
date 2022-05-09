package com.github.caay2000.ttk.integration

import com.github.caay2000.ttk.App
import com.github.caay2000.ttk.context.shared.domain.VehicleTypeEnum
import com.github.caay2000.ttk.context.vehicle.configuration.domain.VehicleConfiguration
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

@TestInstance(PER_CLASS)
class AppIntegrationTest {

    @CsvSource(
        "B, 5",
        "A, 5",
        "AB, 5",
        "AA, 13",
        "BB, 5",
        "BBB, 15",
        "ABABA, 21",
        "ABB, 7",
        "AABABBAB, 29",
        "ABBBABAAABBB, 41"
    )
    @ParameterizedTest(name = "Exercise 1 - {index} - {0} route takes {1} steps")
    fun `exercise 1 - route takes the correct steps`(deliveries: String, steps: Int) {

        val exercise1Configuration = setOf(
            VehicleConfiguration.create(VehicleTypeEnum.TRUCK, 0, 1.0, 1),
            VehicleConfiguration.create(VehicleTypeEnum.BOAT, 0, 1.0, 1)
        )

        val result = App().invoke(input = deliveries, vehicleConfiguration = exercise1Configuration)
        assertThat(result.duration).isEqualTo(steps)
    }

    @CsvSource(
        "B, 7", // 1 + 5 + 1
        "A, 13", // 1 + 1 + 1 + 2 + 6 + 2
        "AB, 13", // 1 + 1 + 1 + 2 + 6 + 2
//        "AA, 13", // 1 + 1 + 1 + 2 + 6 + 2
        "BB, 7", // 1 + 5 + 1
        "BBB, 19", // 1 + 5 + 1 + 5 + 1 + 5 + 1
//        "ABABA, 21",
//        "ABB, 13",
//        "AABABBAB, 29",
//        "ABBBABAAABBB, 41"
    )
    @ParameterizedTest(name = "Exercise 2 {index} - {0} route takes {1} steps")
    fun `exercise 2 - route takes the correct steps`(deliveries: String, steps: Int) {

        val exercise2Configuration = setOf(
            VehicleConfiguration.create(VehicleTypeEnum.TRUCK, 1, 1.0, 1),
            VehicleConfiguration.create(VehicleTypeEnum.BOAT, 2, 0.666666, 4)
        )

        val result = App().invoke(input = deliveries, vehicleConfiguration = exercise2Configuration)
        assertThat(result.duration).isEqualTo(steps)
    }
}
