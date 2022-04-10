package org.github.rafael59r2.isel.webclient.http.models

import java.util.*
import java.util.regex.Pattern

data class URL(val schema: String, val host: String, val port: Int, val path: String, val query: String) {
    companion object {
        // Create a URL object from a string
        fun of(url: String): URL {

            // URL regex
            val pattern = Pattern.compile("(?:([A-Za-z]+)://)?([a-zA-Z0-9.]+):?([0-9]+)?(/[^?]*)?(?:\\?([^#]*))?")

            // Match the URL
            val matcher = pattern.matcher(url)

            // Check if it could be parsed
            if (matcher.matches()) {
                // URL schema
                val proto = (matcher.group(1) ?: "http").lowercase()

                // URL host
                val host = matcher.group(2)

                // URL port
                val port = Integer.parseInt(
                    matcher.group(3) ?: when (proto) {
                        "https" -> "443"
                        "http" -> "80"
                        else -> throw Exception(
                            "Unsupported protocol"
                        )
                    }
                )

                // URL path
                val path = matcher.group(4) ?: "/"

                // URL query
                val query = matcher.group(5) ?: ""

                // Return the URL
                return URL(proto, host, port, path, query)
            }
            // Throw an exception if the URL could not be parsed
            throw Exception("Invalid URL")
        }
    }
}
