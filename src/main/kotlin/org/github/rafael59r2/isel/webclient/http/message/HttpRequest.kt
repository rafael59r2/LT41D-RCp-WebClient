package org.github.rafael59r2.isel.webclient.http.message

import java.net.URL

class HttpRequest private constructor(private val builder: Builder) {

    companion object{
        val defaultHeaders: HashMap<String, String> = HashMap<String, String>().apply {
            this["User-Agent"] = "RCpClient/1.0"
            this["Accept-Language"] = "en"
            this["Accept"] = "*/*"
            this["Connection"] = "Close"
            // this["Accept-Encoding"] = "gzip"
        }
    }

    var port = if(builder.url.port == -1) builder.url.defaultPort else builder.url.port

    var headers = builder.headers

    var method = builder.method

    var url = builder.url

    var body = builder.body

    var cookies = builder.cookies

    var redirect = builder.redirect

    init {
        builder.headers.putIfAbsent("Host", url.host)
        if(body != "") headers.putIfAbsent("Content-Length", body.length.toString())
        defaultHeaders.forEach(headers::putIfAbsent)
    }

    override fun toString(): String {
        var raw = ""
        raw += "$method ${url.path.ifEmpty { "/" }} HTTP/1.1\r\n"

        for (header in headers) {
            raw += "${header.key}: ${header.value}\r\n"
        }

        if (cookies.isNotEmpty()){
            var cookieStr = ""
            for (cookie in cookies){
                cookieStr += "${cookie.key}=${cookie.value};"
            }
            cookieStr += "\r\n"
        }
        raw += "\r\n"
        raw += body
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

        var redirect: Boolean = false
            private set

        fun method(method: Method) = apply { this.method = method }

        fun url(url: URL) = apply { this.url = url }

        fun headers(headers: HashMap<String, String>) = apply { this.headers = headers }

        fun cookies(cookies: HashMap<String, String>) = apply { this.cookies = cookies }

        fun body(body: String) = apply { this.body = body }

        fun followRedirect(redirect: Boolean) = apply { this.redirect = redirect }

        fun build() = HttpRequest(this)
    }
}