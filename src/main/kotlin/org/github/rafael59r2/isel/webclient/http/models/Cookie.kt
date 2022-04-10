package org.github.rafael59r2.isel.webclient.http.models

import java.time.Instant

typealias CookieJar = List<Cookie>
data class Cookie(val name: String, val value: String, val domain: String,  val expires: Instant?, val secure: Boolean)
