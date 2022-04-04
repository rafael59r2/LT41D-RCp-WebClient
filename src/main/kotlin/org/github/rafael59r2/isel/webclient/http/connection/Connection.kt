package org.github.rafael59r2.isel.webclient.http.connection

import java.net.Socket

object Connection{
    fun <T> tcpConnection(host: String, port: Int, callback: (Socket)->T) : T {
        return Socket(host, port).use(callback)
    }
}