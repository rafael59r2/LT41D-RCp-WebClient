package org.github.rafael59r2.isel.webclient.http.connection

import java.net.Socket
import javax.net.SocketFactory
import javax.net.ssl.SSLSocketFactory

object Connection{
    // Creates a TCP connection to the given host and port.
    fun <T> tcpConnection(host: String, port: Int, tls: Boolean = false, callback: (Socket)->T) : T {
        /*
         * If the connection is to be encrypted, we use the default SSL socket factory.
         * Otherwise, we use the default TCP socket factory.
         */
        val socket: Socket = if(!tls) {
            SocketFactory.getDefault().createSocket(host, port)
        } else {
            SSLSocketFactory.getDefault().createSocket(host, port)
        }

        // We return the result of the callback.
        return socket.use(callback)
    }
}