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
import org.github.rafael59r2.isel.webclient.http.models.Method
import org.github.rafael59r2.isel.webclient.http.message.HttpRequest
import org.github.rafael59r2.isel.webclient.http.models.URL
import kotlin.test.Test

class RequestTests {
    companion object {
        // Test server http port
        const val PORT = 8080
        // Test server host
        const val HOST = "127.0.0.1"
        // Test server object
        var server: NettyApplicationEngine? = null

        // Expectations
        const val EXPECTED_GET = "Hello, world!"
        const val EXPECTED_POST = "ABCDEFEADGADFGSDF"
        const val EXPECTED_PATCH = "PATCH!!"
        const val COOKIE_VALUE  = "TESTED"
    }

    init {
        // Starts the test server if there is no server running
        if(server == null) {
            // Creates a new server
            server = embeddedServer(Netty, port = PORT, host = HOST) {
                // Installs default headers feature
                install(DefaultHeaders)

                // Configures the routes
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
                    get("/cookie"){
                        call.respondText(call.request.cookies["test"].orEmpty())
                    }
                }
            }.start(wait = false)
        }
    }


    @Test
    fun `Test Simple GET Request`() {
        // Creates a new HttpRequest object
        val request = HttpRequest.Builder()
            .url(URL.of("http://$HOST:$PORT/"))
            .method(Method.GET)
            .build()
        // Sends the request
        val response = HttpClient.sendRequest(request)
        // Checks the response
        assert(response.body == EXPECTED_GET)
    }

    @Test
    fun `Test Simple POST Request`() {
        // Creates a new HttpRequest object
        val request = HttpRequest.Builder()
            .url(URL.of("http://$HOST:$PORT/"))
            .method(Method.POST)
            .body(EXPECTED_POST)
            .build()
        // Sends the request
        val response = HttpClient.sendRequest(request)
        // Checks the response
        assert(response.body == EXPECTED_POST)
    }

    @Test
    fun `Test Simple PATCH Request`() {
        // Creates a new HttpRequest object
        val request = HttpRequest.Builder()
            .url(URL.of("http://$HOST:$PORT/"))
            .method(Method.PATCH)
            .body(EXPECTED_POST)
            .build()
        // Sends the request
        val response = HttpClient.sendRequest(request)
        // Checks the response
        assert(response.body == EXPECTED_PATCH)
    }


    @Test
    fun `Test Simple HEAD Request`(){
        // Creates a new HttpRequest object
        val request = HttpRequest.Builder()
            .url(URL.of("http://$HOST:$PORT/"))
            .method(Method.HEAD)
            .build()
        // Sends the request
        val response = HttpClient.sendRequest(request)
        // Checks the response
        assert(response.body.isEmpty())
        assert(!response.headers.containsKey("Content-Length") || response.headers["Content-Length"]?.toInt() == 0 || response.headers["Content-Length"]?.toInt() == EXPECTED_GET.length)
    }

    @Test
    fun `Test Simple OPTIONS Request`(){
        // Creates a new HttpRequest object
        val request = HttpRequest.Builder()
            .url(URL.of("http://$HOST:$PORT/"))
            .method(Method.OPTIONS)
            .build()
        // Sends the request
        val response = HttpClient.sendRequest(request)
        // Checks the response
        assert(response.headers.containsKey("Allow"))
    }

    @Test
    fun `Test Simple Cookies`(){
        // Creates a new HttpRequest object
        val builder = HttpRequest.Builder()
            .url(URL.of("http://$HOST:$PORT/cookie"))
            .method(Method.GET)
            .cookie("test", COOKIE_VALUE)
        // Sends the request
        val response = HttpClient.sendRequest(builder.build())
        // Checks the response
        assert(response.body == COOKIE_VALUE)
    }

}