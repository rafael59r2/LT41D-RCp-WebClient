package org.github.rafael59r2.isel.webclient.http.message

import java.net.URL

class HttpRequest private constructor(private val builder: Builder) {

    companion object{
        val defaultHeaders: HashMap<String, String> = HashMap<String, String>().apply {
            this["User-Agent"] = "RCpClient/1.0"
            this["Accept-Language"] = "en"
            this["Accept"] = "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8"
            //this["Accept-Encoding"] = "deflate"
        }
    }

    val port = if(builder.url.port == -1) builder.url.defaultPort else builder.url.port

    val host = builder.url.host

    val method = builder.method

    init {
        builder.headers.putIfAbsent("Host", builder.url.host)
        if(builder.body != "") builder.headers.putIfAbsent("Content-Length", builder.body.length.toString())
        defaultHeaders.forEach(builder.headers::putIfAbsent)
    }

    override fun toString(): String {
        var raw = ""
        raw += "${builder.method} ${builder.url.path.ifEmpty { "/" }} HTTP/1.1\r\n"

        for (header in builder.headers) {
            raw += "${header.key}: ${header.value}\r\n"
        }

        if (builder.cookies.isNotEmpty()){
            var cookieStr = ""
            for (cookie in builder.cookies){
                cookieStr += "${cookie.key}=${cookie.value};"
            }
            cookieStr += "\r\n"
        }
        raw += "\r\n"
        raw += builder.body
        return raw
    }

    class Builder {

        var method: Method = Method.GET
            private set

        var url: URL = URL("http://127.0.0.1")
            private set

        var headers: HashMap<String, String> = HashMap()
            private set

        var cookies: HashMap<String, String> = HashMap()
            private set

        var body: String = ""
            private set

        fun method(method: Method) = apply { this.method = method }

        fun url(url: URL) = apply { this.url = url }

        fun headers(headers: HashMap<String, String>) = apply { this.headers = headers }

        fun cookies(cookies: HashMap<String, String>) = apply { this.cookies = cookies }

        fun body(body: String) = apply { this.body = body }

        fun build() = HttpRequest(this)
    }
}