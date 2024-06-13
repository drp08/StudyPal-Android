package io.github.drp08.studypal.database

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue


class UserTest {

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
            User.deleteAll()
        }
    }

    @Test
    fun `adds user to database`() {
        val success = addUser("Nishant")
        assertTrue(success)
    }

    @Test
    fun `gets a user from database`() {
        addUser("Nishant")

        val user = getUser("Nishant")
        assertNotNull(user)

        assertEquals("Nishant", user.first)
        assertEquals(0, user.second)
    }

    @Test
    fun `returns null if user does not exist`() {
        addUser("Nishant")

        val user = getUser("Harini")
        assertNull(user)
    }

    @Test
    fun `updates the xp of the user`() {
        addUser("Nishant")

        val user = getUser("Nishant")
        assertNotNull(user)
        assertEquals("Nishant", user.first)
        assertEquals(0, user.second)

        val success = updateXp("Nishant", 100)
        assertTrue(success)

        val user2 = getUser("Nishant")
        assertNotNull(user2)
        assertEquals(100, user2.second)
    }
}