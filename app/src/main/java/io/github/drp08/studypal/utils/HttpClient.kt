package io.github.drp08.studypal.utils

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.resources.Resources
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json

val client by lazy {
    HttpClient(Android) {
        install(Resources)
        install(ContentNegotiation) {
            json()
        }
        defaultRequest {
            host = "0.0.0.0" // TODO this needs to change
            port = 8080
            url {
                protocol = URLProtocol.HTTP
            }
        }
    }
}