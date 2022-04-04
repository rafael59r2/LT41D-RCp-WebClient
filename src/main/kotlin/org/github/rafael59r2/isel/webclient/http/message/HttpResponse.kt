package org.github.rafael59r2.isel.webclient.http.message

import java.io.BufferedReader

class HttpResponse private constructor(
    val status: String,
    val headers: HashMap<String, String>,
    val body: ByteArray?,
    val requestMethod: Method
) {
    companion object {
         fun parse(reader: BufferedReader, method: Method): HttpResponse {
            val responseStatus = reader.readLine()
            val headers = HashMap<String, String>()
            var line: String
            while (reader.readLine().also { line = it } != "") {
                val header = line.split(":").map(String::trim)
                headers[header[0]] = header[1]
            }

            val size = headers["Content-Length"]?.toInt() ?: 0

            var content: ByteArray? = null
            if (method != Method.OPTIONS &&
                /* method != Method.TRACE &&*/
                method != Method.HEAD
            ) {
                content = ByteArray(size)
                repeat(size) {
                    content[it] = reader.read().toByte()
                }
            }

            return HttpResponse(responseStatus, headers, content, method)
        }
    }
}