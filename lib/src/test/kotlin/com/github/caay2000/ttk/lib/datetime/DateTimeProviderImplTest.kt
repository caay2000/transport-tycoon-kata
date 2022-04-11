package com.github.caay2000.ttk.lib.datetime

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class DateTimeProviderImplTest {

    @Test
    fun `date time is not shared`() {

        val sut = DateTimeProviderImpl()

        val dateTime = sut.now()

        Assertions.assertThat(dateTime).isEqualTo(DateTime(0))
    }
}
