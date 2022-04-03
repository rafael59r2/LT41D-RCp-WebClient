package org.github.rafael59r2.isel.webclient

import org.github.rafael59r2.isel.webclient.http.HttpClient
import org.github.rafael59r2.isel.webclient.http.Method
import org.github.rafael59r2.isel.webclient.http.messages.HttpRequest
import java.net.URL

suspend fun main(args: Array<String>) {
    val request = HttpRequest.Builder()
        .url(URL("http://example.com"))
        .method(Method.POST)
        .body("Teste")
        .build()

    //println(request)
    println(String(HttpClient.sendRequest(request).await().body))
}