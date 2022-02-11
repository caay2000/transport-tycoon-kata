package com.github.caay2000.ttk.lib.datetime

import com.github.caay2000.ttk.context.time.domain.DateTime
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class DateTimeProviderImplTest {

    @Test
    fun `date time is not shared`() {

        val sut = DateTimeProviderImpl()

        val dateTime = sut.now()

        assertThat(dateTime).isEqualTo(DateTime(0))
    }
}
