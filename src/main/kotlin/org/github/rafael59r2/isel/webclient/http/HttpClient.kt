package org.github.rafael59r2.isel.webclient.http

import org.github.rafael59r2.isel.webclient.http.connection.Connection
import org.github.rafael59r2.isel.webclient.http.message.HttpRequest
import org.github.rafael59r2.isel.webclient.http.message.HttpResponse
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter

object HttpClient {
    fun sendRequest(request: HttpRequest): HttpResponse {
        return Connection.tcpConnection(request.url.host, request.port, request.url.protocol.equals("https", false)) { socket ->
            val writer = PrintWriter(socket.getOutputStream())
            writer.print(request.toString())
            writer.flush()
            val reader = BufferedReader(
                InputStreamReader(
                    socket.getInputStream(), Charsets.UTF_8
                )
            )
            return@tcpConnection HttpResponse.parse(reader, request)
        }
    }
}
