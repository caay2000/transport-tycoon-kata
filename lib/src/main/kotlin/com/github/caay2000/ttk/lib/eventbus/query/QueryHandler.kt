package com.github.caay2000.ttk.lib.eventbus.query

interface QueryHandler<in QUERY : Query, out RESPONSE : Any> {

    fun handle(query: QUERY): RESPONSE
}
