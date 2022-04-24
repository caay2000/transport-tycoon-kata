package com.github.caay2000.ttk.lib.eventbus.query

import com.github.caay2000.ttk.lib.eventbus.impl.KTQueryExecutor

class QueryBusImpl : QueryBus {

    private val queryExecutor = KTQueryExecutor()
    override fun <QUERY : Query, RESPONSE : Any> execute(query: QUERY): RESPONSE {
        println(query)
        val result: RESPONSE = queryExecutor.execute(query)
        println(result)
        return result
    }
}
