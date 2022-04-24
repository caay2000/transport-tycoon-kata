// package com.github.caay2000.ttk.context.time.application
//
// import com.github.caay2000.ttk.lib.eventbus.domain.DateTimeProvider
// import com.github.caay2000.ttk.context.time.domain.DateTime
// import com.github.caay2000.ttk.lib.datetime.DateTimeProviderImpl
// import org.assertj.core.api.Assertions.assertThat
// import org.junit.jupiter.api.Test
//
// internal class UpdateDateTimeCommandHandlerTest {
//
//    private val dateTimeProvider: DateTimeProvider = DateTimeProviderImpl()
//
//    private val sut = UpdateDateTimeCommandHandler(dateTimeProvider)
//
//    @Test
//    fun `UpdateDateTimeCommand updates the time correctly`() {
//
//        assertThat(dateTimeProvider.now()).isEqualTo(DateTime(0))
//
//        sut.invoke(UpdateDateTimeCommand())
//
//        assertThat(dateTimeProvider.now()).isEqualTo(DateTime(1))
//    }
// }
