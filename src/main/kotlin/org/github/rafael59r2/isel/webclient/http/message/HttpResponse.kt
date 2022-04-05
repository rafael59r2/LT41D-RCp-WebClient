package org.github.rafael59r2.isel.webclient.http.message

import org.github.rafael59r2.isel.webclient.http.HttpClient
import org.github.rafael59r2.isel.webclient.http.Utils
import java.io.BufferedReader
import java.net.URL
import java.nio.CharBuffer
import java.security.cert.X509Certificate

class HttpResponse private constructor(
    val status: List<String>,
    val headers: HashMap<String, String>,
    val body: String,
    val requestMethod: Method
) {
    companion object {
        fun parse(reader: BufferedReader, request: HttpRequest): HttpResponse {
            val responseStatus = reader.readLine().split(" ", limit = 3)
            println(responseStatus)
            val headers = HashMap<String, String>()
            var line: String
            while (reader.readLine().also { line = it } != "") {
                println(line)
                val header = line.split(":", limit = 2).map(String::trim)
                headers[header[0]] = header[1]
            }

            val size = headers["Content-Length"]?.toInt() ?: 0


            var content = ""
            if (request.method != Method.OPTIONS &&
                /* method != Method.TRACE &&*/
                request.method != Method.HEAD
            ) {
                if (headers["Transfer-Encoding"].equals("chunked", true)) {
                    content = Utils.parseChunked(reader)
                } else {
                    repeat(size) {
                        content += reader.read().toChar()
                    }
                }
            }

            if ((responseStatus[1] == "302" || responseStatus[1] == "301") && request.redirect) {
                val newRequest = HttpRequest.Builder()
                    .url(URL(headers["Location"]))
                    .method(request.method)
                    .body(request.body)
                    .followRedirect(true)
                    .build()
                return HttpClient.sendRequest(newRequest)
            }

            return HttpResponse(responseStatus, headers, content, request.method)
        }
    }
}