package io.github.drp08.studypal.utils

import androidx.compose.runtime.compositionLocalOf
import io.github.drp08.studypal.db.ClientDatabase

val LocalDatabase = compositionLocalOf<ClientDatabase> { error("No Client Database found!") }