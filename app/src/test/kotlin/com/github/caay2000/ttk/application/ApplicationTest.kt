package com.github.caay2000.ttk.application

import com.github.caay2000.ttk.api.inbound.Delivery
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

internal class ApplicationTest {

    @Test
    @Disabled
    fun `delivers cargo to WAREHOUS_B`() {

        val sut = Application()

        val steps = sut.execute(listOf(Delivery.WAREHOUSE_B))

        assertThat(steps).isEqualTo(5)
    }
}
