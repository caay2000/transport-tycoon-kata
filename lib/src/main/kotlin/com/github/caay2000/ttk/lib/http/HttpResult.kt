package com.github.caay2000.ttk.lib.http

data class HttpResult(val code: HttStatusCode, val body: Any? = null) {
    companion object {
        fun ok(body: Any? = null) = HttpResult(HttStatusCode.OK, body)
        fun notFound() = HttpResult(HttStatusCode.NOT_FOUND)
        fun ko(body: Any? = null) = HttpResult(HttStatusCode.INTERNAL_SERVER_ERROR, body)
    }
}

data class HttpRequest(val method: HttpMethod, val url: String)

enum class HttpMethod { GET, POST, PUT }

enum class HttStatusCode(httpCode: Int) {
    OK(200),
    NOT_FOUND(404),
    INTERNAL_SERVER_ERROR(500)
}
