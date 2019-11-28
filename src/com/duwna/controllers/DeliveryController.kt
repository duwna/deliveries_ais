package com.duwna.controllers

import com.duwna.database.DataBaseHandler
import com.duwna.models.Order
import com.duwna.models.Penalty
import com.duwna.utils.getDayDifference
import com.duwna.utils.showAlert
import javafx.beans.property.SimpleObjectProperty
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import java.net.URL
import java.util.*


class DeliveryController : Initializable {

    @FXML
    lateinit var tableOrder: TableView<Order>
    @FXML
    lateinit var columnIdOrder: TableColumn<Order, Int>
    @FXML
    lateinit var columnOrderDate: TableColumn<Order, String>
    @FXML
    lateinit var columnExpectedDate: TableColumn<Order, String>
    @FXML
    lateinit var columnDeliveryDate: TableColumn<Order, String>
    @FXML
    lateinit var labelIdOrder: Label
    @FXML
    lateinit var datePicker: DatePicker
    @FXML
    lateinit var btnSetDate: Button
    @FXML
    lateinit var btnDeleteOrder: Button
    @FXML
    lateinit var tableContent: TableView<Triple<String, Int, Float>>
    @FXML
    lateinit var columnContentDetail: TableColumn<Triple<String, Int, Float>, String>
    @FXML
    lateinit var columnContentQuantity: TableColumn<Triple<String, Int, Float>, Int>
    @FXML
    lateinit var columnContentSum: TableColumn<Triple<String, Int, Float>, Float>
    @FXML
    lateinit var labelProvider: Label
    @FXML
    lateinit var labelPenalty: Label
    @FXML
    lateinit var labelSum: Label


    override fun initialize(location: URL?, resources: ResourceBundle?) {
        initOrderTable()
        initContentTable()
        showOrders()

        tableOrder.setOnMouseClicked {
            val order = tableOrder.selectionModel.selectedItem
            if (order != null) {
                val idOrder = order.idOrder!!
                showContent(idOrder)
                labelProvider.text = DataBaseHandler.getProviderByOrder(idOrder).name
                labelIdOrder.text = idOrder.toString()
                val penalty = DataBaseHandler.getPenaltyByOrderId(idOrder)
                labelPenalty.text = penalty?.toString() ?: "0"
                labelSum.text = DataBaseHandler.getSumByOrderId(idOrder).toString()
            }
        }

        btnSetDate.setOnAction {
            val order = tableOrder.selectionModel.selectedItem
            if (order != null) {
                if (order.deliveryDate != null) {
                    showAlert("Заказ уже доставлен.")
                    return@setOnAction
                }
                val deliveryDate = datePicker.value.toString()
                val idOrder = order.idOrder!!
                val days = getDayDifference(order.expectedDate, deliveryDate)
                if (days > 0) {
                    val sum = DataBaseHandler.getSumByOrderId(idOrder)
                    val penaltySum = sum * days * 0.001
                    val finalPenalty = if (penaltySum < sum * 0.1) penaltySum else sum * 0.1
                    DataBaseHandler.insertPenalty(Penalty(
                            null,
                            order.idOrder,
                            finalPenalty.toFloat()
                    ))
                    val provider = DataBaseHandler.getProviderByOrder(idOrder)
                    var rating = provider.rating
                    rating -= 0.2f
                    DataBaseHandler.updateRating(provider.idProvider!!, rating)
                    showAlert("Неустойка: $finalPenalty. Рейтинг: $rating")
                    labelPenalty.text = finalPenalty.toString()
                }
                DataBaseHandler.setDeliveryDate(idOrder, deliveryDate)
            }
            showOrders()
        }

        btnDeleteOrder.setOnAction {
            val order = tableOrder.selectionModel.selectedItem
            if (order != null) {
                if (order.deliveryDate != null) {
                    showAlert("Заказ уже доставлен.")
                    return@setOnAction
                }
                val idOrder = order.idOrder!!
                DataBaseHandler.deleteContentOrder(idOrder)
                DataBaseHandler.deleteOrder(idOrder)
                showOrders()
                showContent(idOrder)
            }
        }
    }

    private fun initOrderTable() {
        columnIdOrder.cellValueFactory = PropertyValueFactory(Order.ID_ORDER)
        columnOrderDate.cellValueFactory = PropertyValueFactory(Order.ORDER_DATETIME)
        columnExpectedDate.cellValueFactory = PropertyValueFactory(Order.EXPECTED_DATE)
        columnDeliveryDate.setCellValueFactory { cellData ->
            return@setCellValueFactory SimpleObjectProperty(
                    cellData.value.deliveryDate ?: "Не доставлена")
        }
    }

    private fun initContentTable() {
        columnContentDetail.setCellValueFactory { cellData ->
            return@setCellValueFactory SimpleObjectProperty(cellData.value.first)
        }
        columnContentQuantity.setCellValueFactory { cellData ->
            return@setCellValueFactory SimpleObjectProperty(cellData.value.second)
        }
        columnContentSum.setCellValueFactory { cellData ->
            return@setCellValueFactory SimpleObjectProperty(cellData.value.third)
        }
    }

    private fun showOrders() {
        tableOrder.items = DataBaseHandler.getOrderList()
    }

    private fun showContent(idOrder: Int) {
        tableContent.items = DataBaseHandler.getContentOrderList(idOrder)
    }


}