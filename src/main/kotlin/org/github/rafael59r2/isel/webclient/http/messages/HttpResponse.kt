package org.github.rafael59r2.isel.webclient.http.messages

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader

class HttpResponse private constructor(val status: String, val headers: HashMap<String, String>, val body: ByteArray) {
    companion object {
        suspend fun parse(reader: BufferedReader): HttpResponse {
            val responseStatus = withContext(Dispatchers.IO) {
                reader.readLine()
            }
            val headers = HashMap<String, String>()
            var line: String
            while (withContext(Dispatchers.IO) {
                    reader.readLine()
                }.also { line = it } != "") {
                val header = line.split(":").map(String::trim)
                headers[header[0]] = header[1]
            }

            val size = headers["Content-Length"]?.toInt() ?: 0

            val content = ByteArray(size)

            content.indices.forEach {
                content[it] = reader.read().toByte()
            }

            return HttpResponse(responseStatus, headers, content)
        }
    }
}