package com.github.caay2000.ttk.lib.database

class InMemoryDatabase {

    private val database: MutableMap<String, Map<String, Any>> = mutableMapOf()

    fun save(table: String, key: String, value: Any): Any {
        val actualTable: Map<String, Any>? = database[table]
        if (actualTable == null) {
            database[table] = mapOf(key to value)
        } else {
            database[table] = actualTable + mapOf(key to value)
        }

        return value
    }

    fun exists(table: String, key: String) = database[table]?.containsKey(key) ?: false

    fun getById(table: String, id: String): Any? = database[table]?.get(id)
    fun getAll(table: String): List<Any> = database[table]?.values?.toList() ?: emptyList()
}
