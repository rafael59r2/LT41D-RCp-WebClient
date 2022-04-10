package org.github.rafael59r2.isel.webclient.http

import java.io.BufferedReader

object Utils {
    // Parses a chunked response body.
    fun parseChunked(reader: BufferedReader) : String{
        // Chunk size
        var chunkSize = -1
        // Chunked response body
        var content = ""

        // Repeats until the end of the response body
        while (chunkSize != 0) {
            // Reads the chunk size
            chunkSize = reader.readLine().toInt(16)

            // Reads the chunk
            repeat(chunkSize){
                content += reader.read().toChar()
            }

            // Reads the CRLF
            repeat(2){
                reader.read()
            }
        }

        // Returns the response body
        return content
    }
}