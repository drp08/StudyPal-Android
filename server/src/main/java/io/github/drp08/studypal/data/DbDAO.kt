package io.github.drp08.studypal.data

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

object User : Table() {
    val name = varchar("name", 50).uniqueIndex()
    val xp = integer("xp")
}

object Friends : Table() {
    val name = varchar("name", 50).uniqueIndex()
    val friendName = varchar("friend_name", 50)
}

fun addUser(name: String): Boolean {
    val transaction = transaction {
        User.insert {
            it[User.name] = name
            it[User.xp] = 0
        }
    }
    return transaction.insertedCount == 1
}

fun getUser(name: String): Pair<String, Int> {
    return transaction {
        User.selectAll().where { User.name eq name }.map { it[User.name] to it[User.xp] }.first()
    }
}

fun updateXp(name: String, xp: Int): Boolean {
    val transaction = transaction {
        User.update({ User.name eq name }) {
            it[User.xp] = xp
        }
    }
    return transaction == 1
}

fun getFriends(name: String): List<String> {
    return transaction {
        Friends.selectAll().where { Friends.name eq name }.map { it[Friends.friendName] }
    }
}