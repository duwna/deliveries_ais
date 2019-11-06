package database

import com.duwna.models.User
import java.sql.*

object DataBaseHandler {

    private lateinit var connection: Connection
    private lateinit var statement: Statement
    private lateinit var resultSet: ResultSet

    var isConnected = false
    fun makeConnection(url: String, user: String, pass: String) {
        connection = DriverManager.getConnection(url, user, pass)
        statement = connection.createStatement()
    }

    fun getUserByLogin(login: String): User? {
        resultSet = statement.executeQuery("SELECT * FROM User WHERE login = '$login'")
        return if (resultSet.next()) User(
                resultSet.getInt(1),
                resultSet.getString(2),
                resultSet.getString(3),
                resultSet.getShort(4),
                resultSet.getString(5),
                resultSet.getString(6),
                resultSet.getLong(7)
        ) else null
    }

    fun insertUser(user: User) {
        statement.executeUpdate(user.makeInsertString())
    }

    fun deleteUser(id: Int): Int = statement.executeUpdate("DELETE FROM User WHERE idUser = $id")

}