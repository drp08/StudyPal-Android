package io.github.drp08.studypal.utils

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.resources.Resources
import io.ktor.client.request.headers
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json


val client by lazy {
    HttpClient(Android) {
        install(Resources)
        install(ContentNegotiation) {
            json()
        }
        install(Logging){
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
        defaultRequest {
            host = "146.169.169.174" // TODO this needs to change
            port = 80
            url {
                protocol = URLProtocol.HTTPS
            }
            headers {
                append(HttpHeaders.ContentType,"application/json")
            }
        }
    }
}