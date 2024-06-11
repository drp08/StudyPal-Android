package io.github.drp08.studypal.data

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction

object Users : IntIdTable() {
    val name = varchar("name", 50)
    val xp = integer("xp")
}

class User(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<User>(Users)

    var name by Users.name
    var xp by Users.xp
}

object Friends : IntIdTable() {
    val name = varchar("name", 50)
    val friendName = varchar("friend", 50)
}

class Friend(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Friend>(Friends)

    var name by Friends.name
    var friend by Friends.friendName
}

object DbConnection { // singleton
    val db by lazy {
//        Database.connect("jdbc:sqlite:/data/data.db", driver = "org.sqlite.JDBC")
        Database.connect("jdbc:sqlite::resource:data.db", driver = "org.sqlite.JDBC")
    }
}

fun insertNewUser(name: String) {
    val db = DbConnection.db
    transaction {
        println("Inserting into table!!")
        val user = User.new {
            this.name = name
            this.xp = 0
        }
    }
}

fun getCurrUserName() : String {
    val username = transaction{
        User.all().first().name
    }
    return username
}
fun updateUser(name: String, xp: Int) {
    transaction{
        User.find { Users.name eq name }.first().xp = xp
    }
}

fun addFriendToUser(name: String, friend: String) {
    transaction {
        val friend = Friend.new {
            this.name = name
            this.friend = friend
        }
    }
}

fun getAllUsers() : List<User> {
    val db = DbConnection.db
    val users = transaction {
        User.all().toList()
    }
    return users
}


