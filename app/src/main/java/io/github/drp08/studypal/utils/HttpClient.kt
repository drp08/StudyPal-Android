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
            host = "9d61-2a02-6b6f-f0c5-d400-e394-240a-1849-c9f7.ngrok-free.app" // TODO this needs to change
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