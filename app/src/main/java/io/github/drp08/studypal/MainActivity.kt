package io.github.drp08.studypal

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import io.github.drp08.studypal.presentation.App

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    companion object{
        lateinit var prefs: SharedPreferences;
        var firstTimeOpening: Boolean = true;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        prefs = getSharedPreferences(getString(R.string.app_shared_prefs), Context.MODE_PRIVATE)
        firstTimeOpening = prefs.getBoolean(getString(R.string.first_time_opening), true)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            App()
        }
        prefs?.let { sharedPreferences ->
            if (firstTimeOpening) {
                with (sharedPreferences.edit()) {
                    putBoolean(getString(R.string.first_time_opening), false)
                    apply()
                }
            }
        }
    }
}
