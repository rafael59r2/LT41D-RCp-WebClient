package org.github.rafael59r2.isel.webclient.http.message

import org.github.rafael59r2.isel.webclient.http.models.Method
import org.github.rafael59r2.isel.webclient.http.models.URL

class HttpRequest private constructor(builder: Builder) {

    companion object{
        // Default Request Headers
        val defaultHeaders: HashMap<String, String> = HashMap<String, String>().apply {
            this["User-Agent"] = "RCpClient/1.0"
            this["Accept-Language"] = "en"
            this["Accept"] = "*/*"
            this["Connection"] = "Close"
        }
    }

    // Request Headers
    val headers: Map<String, String>

    // Request Method
    val method = builder.method

    // Request URL
    val url = builder.url

    // Request Port
    val port = url.port

    // Request Body
    val body = builder.body

    // Request Cookies
    val cookies = builder.cookies

    // Follow Redirects
    val redirect = builder.redirect

    init {

        // Set host Header if not set
        builder.headers.putIfAbsent("Host", url.host)

        // Set Content-Length Header if not set and body is not empty
        if(body.isNotEmpty()) builder.headers.putIfAbsent("Content-Length", body.length.toString())

        // Set default headers if not set
        defaultHeaders.forEach(builder.headers::putIfAbsent)
        headers = builder.headers.toMap()
    }

    // Crafts a raw HTTP Request String
    override fun toString(): String {
        // Raw Request String
        var raw = ""

        // Add request line to raw string
        raw += "$method ${url.path.ifEmpty { "/" }}${if(url.query.isNotEmpty()) "?${url.query}" else ""} HTTP/1.1\r\n"

        // Add request headers to raw string
        for (header in headers) {
            raw += "${header.key}: ${header.value}\r\n"
        }

        // If cookies are set, add them to raw string
        if (cookies.isNotEmpty()){
            var cookieStr = "Cookie: "
            for (cookie in cookies){
                cookieStr += "${cookie.key}=${cookie.value};"
            }
            raw += cookieStr + "\r\n"
        }

        // Add empty line to raw string
        raw += "\r\n"

        // Add request body to raw string
        raw += body

        // Return raw string
        return raw
    }

    class Builder {

        // Request method (GET, POST, etc)
        var method: Method = Method.GET
            private set

        // Request URL (http://www.example.com/path/to/resource?query=string)
        var url = URL.of("http://127.0.0.1")
            private set

        // Request headers (key: value)
        val headers: HashMap<String, String> = HashMap()

        // Request cookies (key: value)
        val cookies: HashMap<String, String> = HashMap()

        // Request body (default empty)
        var body: String = ""
            private set

        // Follow redirects (default: false)
        var redirect: Boolean = false
            private set

        // Sets request method
        fun method(method: Method) = apply { this.method = method }

        // Sets request URL
        fun url(url: URL) = apply { this.url = url }

        // Set cookie (key: value)
        fun cookie(name: String, value: String) = apply { this.cookies[name] = value }

        // Set header (key: value)
        fun header(name: String, value: String) = apply { headers[name] = value }

        // Set body
        fun body(body: String) = apply { this.body = body }

        // Set if redirects should be followed
        fun followRedirect(redirect: Boolean) = apply { this.redirect = redirect }

        // Builds HttpRequest
        fun build() = HttpRequest(this)
    }
}