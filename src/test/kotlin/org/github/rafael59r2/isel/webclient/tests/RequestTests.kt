package org.github.rafael59r2.isel.webclient.tests

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.github.rafael59r2.isel.webclient.http.HttpClient
import org.github.rafael59r2.isel.webclient.http.message.Method
import org.github.rafael59r2.isel.webclient.http.message.HttpRequest
import java.net.URL
import kotlin.test.Test

class RequestTests {
    companion object {
        const val PORT = 8080
        const val HOST = "127.0.0.1"
        var server: NettyApplicationEngine? = null
        const val EXPECTED_GET = "Hello, world!"
        const val EXPECTED_POST = "ABCDEFEADGADFGSDF"
        const val EXPECTED_PATCH = "PATCH!!"
    }

    init {
        if(server == null) {
            server = embeddedServer(Netty, port = PORT, host = HOST) {
                install(DefaultHeaders)
                routing {
                    get("/") {
                        call.respondText(EXPECTED_GET)
                    }
                    post("/") {
                        call.respondText(call.receiveText())
                    }
                    patch("/") {
                        call.respondText(EXPECTED_PATCH)
                    }
                    options("/"){
                        call.response.headers.append(HttpHeaders.Allow, "GET, GET, POST")
                    }
                    head("/"){
                        call.response.headers.append(HttpHeaders.ContentLength, EXPECTED_GET.length.toString())
                    }
                }
            }.start(wait = false)
        }
    }


    @Test
    fun `Test Simple GET Request`() {
        val request = HttpRequest.Builder()
            .url(URL("http://$HOST:$PORT/"))
            .method(Method.GET)
            .build()
        val response = HttpClient.sendRequest(request)
        assert(response.body?.let { String(it) }.equals(EXPECTED_GET))
    }

    @Test
    fun `Test Simple POST Request`() {
        val request = HttpRequest.Builder()
            .url(URL("http://$HOST:$PORT/"))
            .method(Method.POST)
            .body(EXPECTED_POST)
            .build()
        val response = HttpClient.sendRequest(request)
        assert(response.body?.let { String(it) }.equals(EXPECTED_POST))
    }

    @Test
    fun `Test Simple PATCH Request`() {
        val request = HttpRequest.Builder()
            .url(URL("http://$HOST:$PORT/"))
            .method(Method.PATCH)
            .body(EXPECTED_POST)
            .build()
        val response = HttpClient.sendRequest(request)
        assert(response.body?.let { String(it) }.equals(EXPECTED_PATCH))
    }


    @Test
    fun `Test Simple HEAD Request`(){
        val request = HttpRequest.Builder()
            .url(URL("http://$HOST:$PORT/"))
            .method(Method.HEAD)
            .build()
        val response = HttpClient.sendRequest(request)
        assert(response.body == null)
        assert(!response.headers.containsKey("Content-Length") || response.headers["Content-Length"]?.toInt() == 0 || response.headers["Content-Length"]?.toInt() == EXPECTED_GET.length)
    }

    @Test
    fun `Test Simple OPTIONS Request`(){
        val request = HttpRequest.Builder()
            .url(URL("http://$HOST:$PORT/"))
            .method(Method.OPTIONS)
            .build()
        val response = HttpClient.sendRequest(request)
        assert(response.headers.containsKey("Allow"))
    }

}