package org.github.rafael59r2.isel.webclient

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.github.rafael59r2.isel.webclient.http.HttpClient
import org.github.rafael59r2.isel.webclient.http.message.Method
import org.github.rafael59r2.isel.webclient.http.message.HttpRequest
import java.net.URL

suspend fun main(args: Array<String>) {
    coroutineScope {
        val request = HttpRequest.Builder()
            .url(URL("https://google.com/"))
            .method(Method.GET)
            .followRedirect(true)
            .build()
        //println(request)
        val futureResponse = async(Dispatchers.IO) { HttpClient.sendRequest(request) }
        println(futureResponse.await().body)
    }
}