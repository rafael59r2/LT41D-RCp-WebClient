package org.github.rafael59r2.isel.webclient.http

import kotlinx.coroutines.*
import org.github.rafael59r2.isel.webclient.http.messages.HttpRequest
import org.github.rafael59r2.isel.webclient.http.messages.HttpResponse
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

object HttpClient {
    suspend fun sendRequest(request: HttpRequest): Deferred<HttpResponse> {
        return coroutineScope {
            async(Dispatchers.IO) {
                val socket = Socket(request.host, request.port)
                socket.use {
                    val writer = PrintWriter(socket.getOutputStream())
                    writer.print(request.toString())
                    writer.flush()
                    val reader = BufferedReader(
                        InputStreamReader(
                            socket.getInputStream()
                        )
                    )
                    return@use HttpResponse.parse(reader)
                }
            }
        }
    }
}