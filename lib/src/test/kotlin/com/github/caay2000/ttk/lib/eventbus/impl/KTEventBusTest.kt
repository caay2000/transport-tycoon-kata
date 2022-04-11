package com.github.caay2000.ttk.lib.eventbus.impl

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class KTEventBusTest {

    @Test
    internal fun `event is published to event bus`() {

        val eventBus = KTEventBus.init<String, String, String>()
        val sut = KTEventPublisher<String>()
        sut.publish("hi")

        assertThat(eventBus.getAllEvents()).hasSize(1)
            .isEqualTo(listOf("hi"))
    }

    @Test
    internal fun `subscribers receive the published event`() {

        KTEventBus.init<String, String, String>()
        val sut = StringSubscriber()
        KTEventPublisher<String>().publish("hi")

        assertThat(sut.events).isEqualTo(listOf("hi"))
    }

    @Test
    internal fun `multiple subscribers receive the published event`() {

        val eventBus = KTEventBus.init<String, String, String>()
        val subscriber1 = StringSubscriber()
        val subscriber2 = StringSubscriber()
        KTEventPublisher<String>().publish("hi")

        assertThat(subscriber1.events).isEqualTo(listOf("hi"))
        assertThat(subscriber2.events).isEqualTo(listOf("hi"))
        assertThat(eventBus.getAllEvents()).isEqualTo(listOf("hi"))
    }

    @Test
    internal fun `subscriber of different type does not receive the event`() {

        val eventBus = KTEventBus.init<String, String, String>()
        val sut = IntSubscriber()
        KTEventPublisher<Number>().publish(Double.MAX_VALUE)

        assertThat(eventBus.getAllEvents()).hasSize(1)
        assertThat(sut.events).hasSize(0)
    }

    inner class StringSubscriber : KTEventSubscriber<String>(String::class) {
        val events = mutableListOf<String>()
        override fun handle(event: String) {
            events.add(event)
        }
    }

    inner class IntSubscriber : KTEventSubscriber<Int>(Int::class) {
        val events = mutableListOf<Int>()
        override fun handle(event: Int) {
            events.add(event)
        }
    }
}
