package io.github.drp08.studypal.data

import org.jetbrains.exposed.sql.Database

object DbConnection {
    val db by lazy {
        Database.connect("jdbc:sqlite:/data/data.db", driver = "org.sqlite.JDBC")
    }
}