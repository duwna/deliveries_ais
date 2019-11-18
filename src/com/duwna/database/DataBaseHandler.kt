package com.duwna.database

import com.duwna.models.Detail
import com.duwna.models.Provider
import com.duwna.models.User
import javafx.collections.FXCollections
import javafx.collections.ObservableList
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
                resultSet.getInt(User.COLUMN_ID_USER),
                resultSet.getString(User.COLUMN_LOGIN),
                resultSet.getString(User.COLUMN_PASSWORD),
                resultSet.getShort(User.COLUMN_POSITION),
                resultSet.getString(User.COLUMN_FIRST_NAME),
                resultSet.getString(User.COLUMN_LAST_NAME),
                resultSet.getLong(User.COLUMN_PHONE)
        ) else null
    }

    fun getUserList(): ObservableList<User> {
        val userList = FXCollections.observableArrayList<User>()
        resultSet = statement.executeQuery("SELECT * FROM User")
        while (resultSet.next()) {
            userList.add(User(
                    resultSet.getInt(User.COLUMN_ID_USER),
                    resultSet.getString(User.COLUMN_LOGIN),
                    resultSet.getString(User.COLUMN_PASSWORD),
                    resultSet.getShort(User.COLUMN_POSITION),
                    resultSet.getString(User.COLUMN_FIRST_NAME),
                    resultSet.getString(User.COLUMN_LAST_NAME),
                    resultSet.getLong(User.COLUMN_PHONE)))
        }
        return userList
    }

    fun getDetailList(): ObservableList<Detail> {
        val detailList = FXCollections.observableArrayList<Detail>()
        resultSet = statement.executeQuery("SELECT * FROM Detail")
        while (resultSet.next()) {
            detailList.add(Detail(
                    resultSet.getInt(Detail.COLUMN_ID_DETAIL),
                    resultSet.getString(Detail.COLUMN_NAME),
                    resultSet.getFloat(Detail.COLUMN_WEIGHT),
                    resultSet.getBoolean(Detail.COLUMN_IS_HIGH_TECH)))
        }
        return detailList
    }

    fun getProviderList(): ObservableList<Provider> {
        val providerList = FXCollections.observableArrayList<Provider>()
        resultSet = statement.executeQuery("SELECT * FROM Provider")
        while (resultSet.next()) {
            providerList.add(Provider(
                    resultSet.getInt(Provider.COLUMN_ID_PROVIDER),
                    resultSet.getString(Provider.COLUMN_NAME),
                    resultSet.getFloat(Provider.COLUMN_RATING),
                    resultSet.getString(Provider.COLUMN_CITY),
                    resultSet.getString(Provider.COLUMN_ADDRESS)))
        }
        return providerList
    }

    fun checkProviderName(name: String): Boolean = statement.executeQuery("SELECT * FROM Provider WHERE name = '$name'").next()
    fun checkDetailName(name: String): Boolean = statement.executeQuery("SELECT * FROM Detail WHERE name = '$name'").next()

    fun insertUser(user: User) = statement.executeUpdate(user.makeInsertString())
    fun insertDetail(detail: Detail) = statement.executeUpdate(detail.makeInsertString())
    fun insertProvider(provider: Provider) = statement.executeUpdate(provider.makeInsertString())

    fun updateProvider(provider: Provider) = statement.executeUpdate(provider.makeUpdateString())
    fun updateDetail(detail: Detail) = statement.executeUpdate(detail.makeUpdateString())

    fun deleteUser(id: Int) = statement.executeUpdate("DELETE FROM User WHERE idUser = $id")
    fun deleteDetail(id: Int) = statement.executeUpdate("DELETE FROM Detail WHERE idDetail = $id")
    fun deleteProvider(id: Int) = statement.executeUpdate("DELETE FROM Provider WHERE idProvider = $id")
}

