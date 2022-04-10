package org.github.rafael59r2.isel.webclient.http

import org.github.rafael59r2.isel.webclient.http.connection.Connection
import org.github.rafael59r2.isel.webclient.http.message.HttpRequest
import org.github.rafael59r2.isel.webclient.http.message.HttpResponse
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter

object HttpClient {
    // Sends a request to the server and return the response
    fun sendRequest(request: HttpRequest): HttpResponse {
        // Creates a connection to the server
        return Connection.tcpConnection(
            request.url.host,
            request.port,
            request.url.schema.equals("https", false)
        ) { socket ->
            // Creates a writer to the socket
            val writer = PrintWriter(socket.getOutputStream())

            // Sends the request to the server
            writer.print(request.toString())
            // Flushes the output stream
            writer.flush()

            // Creates a buffered reader to read the response
            val reader = BufferedReader(
                // Creates a reader to the socket
                InputStreamReader(
                    socket.getInputStream(), Charsets.UTF_8
                )
            )

            // Parses the response and return it
            return@tcpConnection HttpResponse.parse(reader, request)
        }
    }
}
