package io.github.drp08.studypal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.room.Room
import io.github.drp08.studypal.db.ClientDatabase
import io.github.drp08.studypal.presentation.App

class MainActivity : ComponentActivity() {

    val sessionDb by lazy {
        Room.databaseBuilder(
            applicationContext,
            ClientDatabase::class.java,
            "sessionDb"
        ).build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            App(sessionDb.sessionDao)
        }
    }
}
