package com.github.caay2000.ttk.lib.eventbus.query

interface QueryBus {

    fun <QUERY : Query, RESPONSE : Any> execute(query: QUERY): RESPONSE
}
