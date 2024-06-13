package io.github.drp08.studypal.di

import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.drp08.studypal.utils.HttpConstants.HOSTNAME
import io.github.drp08.studypal.utils.HttpConstants.PORT
import io.github.drp08.studypal.utils.HttpConstants.PROTOCOL
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
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HttpModule {
    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
       return HttpClient(Android) {
            install(Resources)
            install(ContentNegotiation) {
                json(
                    json = Json {
                        ignoreUnknownKeys = true
                    }
                )
            }
            install(Logging){
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.d("HttpClient", "log: $message")
                    }
                }
                level = LogLevel.ALL
            }
            defaultRequest {
                url("http://146.169.169.145:8080")
                headers {
                    append(HttpHeaders.ContentType,"application/json")
                }
            }
        }
    }
}