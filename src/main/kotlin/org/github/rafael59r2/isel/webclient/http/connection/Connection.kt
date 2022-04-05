package org.github.rafael59r2.isel.webclient.http.connection

import java.net.Socket
import javax.net.ssl.SSLSocketFactory

object Connection{
    fun <T> tcpConnection(host: String, port: Int, tls: Boolean = false, callback: (Socket)->T) : T {
        println(tls)
        val socket: Socket = if(!tls) {
            Socket(host, port)
        } else {
            SSLSocketFactory.getDefault().createSocket(host, port)
        }
        return socket.use(callback)
    }
}