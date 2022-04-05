package org.github.rafael59r2.isel.webclient.http

import java.io.BufferedReader

object Utils {
    fun parseChunked(reader: BufferedReader) : String{
        var chunkSize = -1
        var content = ""
        while (chunkSize != 0) {
            chunkSize = reader.readLine().toInt(16)
            repeat(chunkSize){
                content += reader.read().toChar()
            }
            repeat(2){
                reader.read()
            }
        }
        return content
    }
}