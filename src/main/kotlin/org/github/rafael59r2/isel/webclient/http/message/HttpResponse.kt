package org.github.rafael59r2.isel.webclient.http.message

import org.github.rafael59r2.isel.webclient.http.HttpClient
import org.github.rafael59r2.isel.webclient.http.Utils
import org.github.rafael59r2.isel.webclient.http.models.Headers
import org.github.rafael59r2.isel.webclient.http.models.Method
import org.github.rafael59r2.isel.webclient.http.models.Status
import org.github.rafael59r2.isel.webclient.http.models.URL
import java.io.BufferedReader

data class HttpResponse constructor(
    val status: Status,
    val headers: Headers,
    val body: String
) {
    companion object {
        // Reads the response from the buffered reader and returns a new HttpResponse
        fun parse(reader: BufferedReader, request: HttpRequest): HttpResponse {
            // Reads the status line
            val responseStatus = reader.readLine().split(" ", limit = 3)

            // Headers variable
            val headers = Headers()

            // Reads the headers from the buffered reader
            var line: String
            while (reader.readLine().also { line = it } != "") {
                val header = line.split(":", limit = 2).map(String::trim)
                if ("Set-Cookie" == header[0]) continue
                headers[header[0]] = header[1]
            }

            // Gets content length from the headers if it exists, otherwise it's 0
            val size = headers["Content-Length"]?.toInt() ?: 0

            // Content variable
            var content = ""

            // Checks if request method is supposed to have a body
            if (request.method != Method.OPTIONS &&
                /* method != Method.TRACE &&*/
                request.method != Method.HEAD
            ) {
                // Check if Transfer-Encoding is chunked
                if (headers["Transfer-Encoding"].equals("chunked", true)) {
                    // If it is, reads the body from the buffered reader using the parseChunked method
                    content = Utils.parseChunked(reader)
                } else {
                    // If it isn't, reads the body from the buffered reader reading byte-by-byte
                    repeat(size) {
                        content += reader.read().toChar()
                    }
                }
            }

            /*
            * Checks if the response status is a redirect and
            * if request is following redirects and if it is,
            * it follows the redirect creating a new request
            */
            if ((responseStatus[1] == "302" || responseStatus[1] == "301" || responseStatus[1] == "201") && request.redirect && headers.containsKey("Location")) {
                // Creates a new request with the location header as the URL
                val newRequest = HttpRequest.Builder()
                    .url(URL.of(headers["Location"]!!))
                    .method(request.method)
                    .body(request.body)
                    .followRedirect(true)
                    .build()
                // Sends the new request and returns the response
                return HttpClient.sendRequest(newRequest)
            }

            // Returns a new HttpResponse with the parsed data
            return HttpResponse(
                Status(responseStatus[0], responseStatus[1].toInt(), responseStatus[2]),
                headers,
                content
            )
        }
    }
}