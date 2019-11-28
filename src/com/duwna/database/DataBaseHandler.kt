package com.duwna.database

import com.duwna.models.*
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement

object DataBaseHandler {

    private lateinit var connection: Connection
    private lateinit var statement: Statement
    private lateinit var resultSet: ResultSet

    var isConnected = false

    fun makeConnection(url: String, user: String, pass: String) {
        connection = DriverManager.getConnection(url, user, pass)
        statement = connection.createStatement()
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

    fun getOrderList(): ObservableList<Order> {
        val orderList = FXCollections.observableArrayList<Order>()
        resultSet = statement.executeQuery("SELECT * FROM `Order`")
        while (resultSet.next()) {
            orderList.add(Order(
                    resultSet.getInt(Order.ID_ORDER),
                    resultSet.getString(Order.ORDER_DATETIME),
                    resultSet.getString(Order.EXPECTED_DATE),
                    resultSet.getString(Order.DELIVERY_DATE)))
        }
        return orderList
    }

    fun getContentOrderList(idOrder: Int): ObservableList<Triple<String, Int, Float>> {
        val contentOrderList = FXCollections.observableArrayList<Triple<String, Int, Float>>()
        resultSet = statement.executeQuery("SELECT Detail.name, ContentOrder.quantity, ContentOrder.sum FROM " +
                "`Order` JOIN ContentOrder ON `Order`.idOrder = ContentOrder.idOrder " +
                "JOIN DetailProvider ON ContentOrder.idDetailProvider = DetailProvider.idDetailProvider " +
                "JOIN Detail ON DetailProvider.idDetail = Detail.idDetail WHERE `Order`.idOrder = $idOrder")
        while (resultSet.next()) {
            contentOrderList.add(Triple(
                    resultSet.getString(1),
                    resultSet.getInt(2),
                    resultSet.getFloat(3))
            )
        }
        return contentOrderList
    }

    fun getDetailPriceList(idProvider: Int): ObservableList<Pair<Detail, DetailProvider>> {
        val detailDetailPrice = FXCollections.observableArrayList<Pair<Detail, DetailProvider>>()
        resultSet = statement.executeQuery("SELECT * FROM DetailProvider JOIN Detail " +
                "ON DetailProvider.idDetail = Detail.idDetail WHERE DetailProvider.idProvider = $idProvider")
        while (resultSet.next()) {

            val detail = Detail(
                    resultSet.getInt(Detail.COLUMN_ID_DETAIL),
                    resultSet.getString(Detail.COLUMN_NAME),
                    resultSet.getFloat(Detail.COLUMN_WEIGHT),
                    resultSet.getBoolean(Detail.COLUMN_IS_HIGH_TECH))

            val detailProvider = DetailProvider(
                    resultSet.getInt(DetailProvider.COLUMN_ID_DETAIL_PROVIDER),
                    resultSet.getInt(DetailProvider.COLUMN_ID_DETAIL),
                    resultSet.getInt(DetailProvider.COLUMN_ID_PROVIDER),
                    resultSet.getFloat(DetailProvider.COLUMN_PRICE))

            detailDetailPrice.add(detail to detailProvider)
        }
        return detailDetailPrice
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

    fun getProviderByOrder(idOrder: Int): Provider {
        resultSet = statement.executeQuery("SELECT * FROM `Order` " +
                "JOIN ContentOrder ON `Order`.idOrder = ContentOrder.idOrder " +
                "JOIN DetailProvider ON ContentOrder.idDetailProvider = DetailProvider.idDetailProvider " +
                "JOIN Provider ON DetailProvider.idProvider = Provider.idProvider " +
                "WHERE `Order`.idOrder = $idOrder LIMIT 1")
        resultSet.next()
        return Provider(
                resultSet.getInt(Provider.COLUMN_ID_PROVIDER),
                resultSet.getString(Provider.COLUMN_NAME),
                resultSet.getFloat(Provider.COLUMN_RATING),
                resultSet.getString(Provider.COLUMN_CITY),
                resultSet.getString(Provider.COLUMN_ADDRESS))
    }

    //"SELECT Provider.rating FROM `Order` JOIN ContentOrder ON `Order`.idOrder = ContentOrder.idOrder JOIN DetailProvider ON ContentOrder.idDetailProvider = DetailProvider.idDetailProvider JOIN Provider ON DetailProvider.idProvider = Provider.idProvider WHERE `Order`.idOrder = $idOrder LIMIT 1"

    fun getPenaltyByOrderId(idOrder: Int): Float? {
        resultSet = statement.executeQuery("SELECT sum FROM Penalty WHERE idOrder = $idOrder")
        return if(resultSet.next()) resultSet.getFloat(1) else null
    }

    fun getSumByOrderId(idOrder: Int) : Float {
        resultSet = statement.executeQuery("SELECT SUM(sum) from `Order` " +
                "JOIN ContentOrder ON `Order`.idOrder = ContentOrder.idOrder " +
                "WHERE `Order`.idOrder = $idOrder;")
        resultSet.next()
        return resultSet.getFloat(1)
    }

    fun checkProviderName(name: String): Boolean = statement.executeQuery("SELECT * FROM Provider WHERE name = '$name'").next()
    fun checkDetailName(name: String): Boolean = statement.executeQuery("SELECT * FROM Detail WHERE name = '$name'").next()
    fun checkDetailProvider(idDetail: Int, idProvider: Int): Boolean = statement
            .executeQuery("SELECT * FROM DetailProvider WHERE idProvider = $idProvider and idDetail = $idDetail").next()

    fun getLastOrderId(): Int = with(statement.executeQuery("SELECT MAX(idOrder) FROM `Order`")) {
        next()
        getInt(1)
    }

    fun insertUser(user: User) = statement.executeUpdate(user.makeInsertString())
    fun insertDetail(detail: Detail) = statement.executeUpdate(detail.makeInsertString())
    fun insertProvider(provider: Provider) = statement.executeUpdate(provider.makeInsertString())
    fun insertDetailProvider(detailProvider: DetailProvider) = statement.executeUpdate(detailProvider.makeInsertString())
    fun insertContentOrder(contentOrder: ContentOrder) = statement.executeUpdate(contentOrder.makeInsertString())
    fun insertOrder(order: Order) = statement.executeUpdate(order.makeInsertString())
    fun insertPenalty(penalty: Penalty) = statement.executeUpdate(penalty.makeInsertString())

    fun updateProvider(provider: Provider) = statement.executeUpdate(provider.makeUpdateString())
    fun updateDetail(detail: Detail) = statement.executeUpdate(detail.makeUpdateString())
    fun setDeliveryDate(idOrder: Int, date: String) {
        statement.executeUpdate("UPDATE `Order` set deliveryDate = '$date' WHERE idOrder = $idOrder")
    }
    fun updateRating(idProvider: Int, rating: Float) = statement.executeUpdate("UPDATE Provider SET rating = $rating WHERE idProvider = $idProvider")

    fun deleteUser(id: Int) = statement.executeUpdate("DELETE FROM User WHERE idUser = $id")
    fun deleteDetail(id: Int) = statement.executeUpdate("DELETE FROM Detail WHERE idDetail = $id")
    fun deleteProvider(id: Int) = statement.executeUpdate("DELETE FROM Provider WHERE idProvider = $id")
    fun deleteDetailProvider(id: Int) = statement.executeUpdate("DELETE FROM DetailProvider WHERE idDetailProvider = $id")
    fun deleteOrder(idOrder: Int) = statement.executeUpdate("DELETE FROM `Order` WHERE idOrder = $idOrder")
    fun deleteContentOrder(idOrder: Int) = statement.executeUpdate("DELETE FROM ContentOrder WHERE idOrder = $idOrder")
}

