package io.github.drp08.studypal.database

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue


class FriendsTest {

    companion object {
        @JvmStatic
        @BeforeClass
        fun setUpClass(): Unit {
            Database.connect(
                url = "jdbc:sqlite::resource:test.db",
                driver = "org.sqlite.JDBC"
            )
        }
    }

    @Before
    fun setUp() {
        transaction {
            Friends.deleteAll()
            User.deleteAll()
            User.insert {
                it[name] = "Nishant"
                it[xp] = 0
            }
            User.insert {
                it[name] = "Harini"
                it[xp] = 0
            }
            User.insert {
                it[name] = "Virginia"
                it[xp] = 0
            }
        }
    }

    @Test
    fun `add a new friend`() {
        assertTrue { addFriend("Nishant", "Harini") }
    }

    @Test
    fun `get a list of friends`() {
        assertTrue { addFriend("Nishant", "Harini") }
        assertTrue { addFriend("Nishant", "Virginia") }

        val friendsNishant = getFriends("Nishant")
        assertContentEquals(listOf("Harini", "Virginia"), friendsNishant)

        val friendsHarini = getFriends("Harini")
        assertContentEquals(listOf("Nishant"), friendsHarini)

        val friendsVirginia = getFriends("Virginia")
        assertContentEquals(listOf("Nishant"), friendsVirginia)
    }

    @Test
    fun `removes a friend`() {
        assertTrue { addFriend("Nishant", "Harini") }
        val friendsNishant = getFriends("Nishant")
        assertContentEquals(listOf("Harini"), friendsNishant)

        val friendsHarini = getFriends("Harini")
        assertContentEquals(listOf("Nishant"), friendsHarini)

        /* --- */

        assertTrue { removeFriend("Harini", "Nishant") }

        val friendsNishantAfterDelete = getFriends("Nishant")
        assertContentEquals(emptyList(), friendsNishantAfterDelete)

        val friendsHariniAfterDelete = getFriends("Harini")
        assertContentEquals(emptyList(), friendsHariniAfterDelete)
    }
}