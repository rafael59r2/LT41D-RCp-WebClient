package org.github.rafael59r2.isel.webclient

import org.github.rafael59r2.isel.webclient.http.HttpClient
import org.github.rafael59r2.isel.webclient.http.message.HttpRequest
import org.github.rafael59r2.isel.webclient.http.models.Method
import org.github.rafael59r2.isel.webclient.http.models.URL


fun main(args: Array<String>) {
    // Infinitive loop
    while (true) {
        // Asks for the request method
        print("Insira o metodo: ")
        val method = readln()

        // Asks for the request URL
        print("Insira o URL: ")
        val url = readln()

        // Asks for the request body
        print("Insira o Body: ")
        val body = readln()

        // Creates the request
        val request = HttpRequest.Builder()
            .url(URL.of(url))
            .method(Method.valueOf(method))
            .body(body)
            .followRedirect(true)
            .build()

        // Sends the request and prints the response headers
        println(HttpClient.sendRequest(request).headers)
    }

}