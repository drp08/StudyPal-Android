package io.github.drp08.studypal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.room.Room
import io.github.drp08.studypal.db.ClientDatabase
import io.github.drp08.studypal.presentation.App
import io.github.drp08.studypal.utils.LocalDatabase

class MainActivity : ComponentActivity() {

    private val clientDb by lazy {
        Room.databaseBuilder(
            applicationContext,
            ClientDatabase::class.java,
            "clientDb"
        ).build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CompositionLocalProvider(LocalDatabase provides clientDb) {
                App()
            }
        }
    }
}
