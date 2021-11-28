package com.github.caay2000.ttk.integration

import com.github.caay2000.ttk.App
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class AppIntegrationTest {

    @Disabled
    @CsvSource(
        "A, 5",
        "AB, 5",
        "BB, 5",
        "ABB, 7",
        "AABABBAB, 29",
        "ABBBABAAABBB, 41"
    )
    @ParameterizedTest(name = "{index} - {0} route takes {1} steps")
    fun `route takes the correct steps`(deliveries: String, steps: Int) {
        val result = App().invoke(deliveries)
        assertThat(result).isEqualTo(steps)
    }
}
