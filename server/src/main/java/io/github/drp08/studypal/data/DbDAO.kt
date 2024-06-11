package io.github.drp08.studypal.data

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
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

fun getUser(name: String): Pair<String, Int>? {
    return transaction {
        User.selectAll().where { User.name eq name }.map { it[User.name] to it[User.xp] }.firstOrNull()
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

fun addFriend(name: String, friendName: String): Boolean {
    val transaction = transaction {
        Friends.insert {
            it[Friends.name] = name
            it[Friends.friendName] = friendName
        }

        Friends.insert {
            it[Friends.name] = friendName
            it[Friends.friendName] = name
        }
    }

    return transaction.insertedCount == 2
}

fun deleteFriend(name: String, friendName: String): Boolean {
    val transaction = transaction {
        Friends.deleteWhere { (Friends.name eq name) and (Friends.friendName eq friendName) }
        Friends.deleteWhere { (Friends.name eq friendName) and (Friends.friendName eq name) }
    }
    return transaction == 2
}