package com.duwna.controllers

import com.duwna.database.DataBaseHandler
import com.duwna.models.*
import com.duwna.utils.showAlert
import com.duwna.utils.sqlFormat
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import java.net.URL
import java.util.*


class OrderController : Initializable {

    @FXML
    lateinit var tableProvider: TableView<Provider>
    @FXML
    lateinit var columnProviderName: TableColumn<Provider, String>
    @FXML
    lateinit var columnRating: TableColumn<Provider, Float>
    @FXML
    lateinit var tableDetail: TableView<Pair<Detail, DetailProvider>>
    @FXML
    lateinit var columnDetailName: TableColumn<Pair<Detail, DetailProvider>, String>
    @FXML
    lateinit var columnPrice: TableColumn<Pair<Detail, DetailProvider>, Float>
    @FXML
    lateinit var columnWeight: TableColumn<Pair<Detail, DetailProvider>, Float>
    @FXML
    lateinit var columnIsHighTech: TableColumn<Pair<Detail, DetailProvider>, String>
    @FXML
    lateinit var tableContent: TableView<Content>
    @FXML
    lateinit var columnContentDetail: TableColumn<Content, String>
    @FXML
    lateinit var columnContentQuantity: TableColumn<Content, Int>
    @FXML
    lateinit var columnContentSum: TableColumn<Content, Float>
    @FXML
    lateinit var labelTotalSum: Label
    @FXML
    lateinit var labelTotalWeight: Label
    @FXML
    lateinit var datePicker: DatePicker
    @FXML
    lateinit var btnCheckout: Button
    @FXML
    lateinit var labelDetail: Label
    @FXML
    lateinit var tfQuantity: TextField
    @FXML
    lateinit var btnAdd: Button
    @FXML
    lateinit var btnDelete: Button

    private val contentList = FXCollections.observableArrayList<Content>()

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        initProviderTable()
        initDetailTable()
        initContentTable()

        tableProvider.setOnMouseClicked {
            val provider = tableProvider.selectionModel.selectedItem
            if (provider != null) {
                showDetails(provider.idProvider!!)
                contentList.clear()
                showContent()
                updateTotal()
            }
        }

        tableDetail.setOnMouseClicked {
            val pair = tableDetail.selectionModel.selectedItem
            if (pair != null) {
                labelDetail.text = pair.first.name
            }
        }

        tableContent.setOnMouseClicked {
            val content = tableContent.selectionModel.selectedItem
            if (content != null) {
                tfQuantity.text = content.quantity.toString()
                labelDetail.text = content.detail.name
            }
        }

        btnDelete.setOnAction {
            val content = tableContent.selectionModel.selectedItem
            if (content != null) {
                contentList.remove(content)
                showContent()
            }
            updateTotal()
        }

        btnAdd.setOnAction {
            val pair = tableDetail.selectionModel.selectedItem
            if (pair != null) {
                if (tfQuantity.text.toIntOrNull() == null || tfQuantity.text.toInt() <= 0) {
                    showAlert("Количество введено некорректно.")
                    return@setOnAction
                }
                if (tableProvider.selectionModel.selectedItem.rating < 15 &&
                        (pair.first.isHighTech || pair.second.price > 1000)) {
                    showAlert("Поставщик не является надежным.")
                    return@setOnAction
                }
                contentList.forEach {
                    if (it.detailProvider.idDetail == pair.second.idDetail) {
                        showAlert("Деталь уже добавлена")
                        return@setOnAction
                    }
                }
                contentList.add(Content(
                        pair.first, pair.second,
                        tfQuantity.text.toInt(),
                        pair.second.price * tfQuantity.text.toInt()))
                showContent()
                updateTotal()
            }
        }

        btnCheckout.setOnAction {
            when {
                contentList.isEmpty() -> showAlert("В составе заказа ничего нет.")
                datePicker.value == null -> showAlert("Дата не указана.")
                labelTotalWeight.text.toFloat() > 1500 -> showAlert("Вес не может превышать 1,5 тонны.")
                else -> {
                    DataBaseHandler.insertOrder(Order(
                            null,
                            Date().sqlFormat(),
                            "${datePicker.value} 12:00:00",
                            null))
                    val idOrder = DataBaseHandler.getLastOrderId()
                    contentList.forEach {
                        DataBaseHandler.insertContentOrder(it.toContentOrder(idOrder))
                    }
                    contentList.clear()
                    showContent()
                    updateTotal()
                    showAlert("Заказ оформлен!")
                }
            }
        }
    }

    private fun initProviderTable() {
        columnProviderName.cellValueFactory = PropertyValueFactory(Provider.COLUMN_NAME)
        columnRating.cellValueFactory = PropertyValueFactory(Provider.COLUMN_RATING)
        tableProvider.items = DataBaseHandler.getProviderList()
    }

    private fun initDetailTable() {
        columnDetailName.setCellValueFactory { cellData ->
            return@setCellValueFactory SimpleObjectProperty(cellData.value.first.name)
        }
        columnWeight.setCellValueFactory { cellData ->
            return@setCellValueFactory SimpleObjectProperty(cellData.value.first.weight)
        }
        columnIsHighTech.setCellValueFactory { cellData ->
            return@setCellValueFactory SimpleObjectProperty(if (cellData.value.first.isHighTech) "Да" else "Нет")
        }
        columnPrice.setCellValueFactory { cellData ->
            return@setCellValueFactory SimpleObjectProperty(cellData.value.second.price)
        }
    }

    private fun initContentTable() {
        columnContentDetail.setCellValueFactory { cellData ->
            return@setCellValueFactory SimpleObjectProperty(cellData.value.detail.name)
        }
        columnContentQuantity.setCellValueFactory { cellData ->
            return@setCellValueFactory SimpleObjectProperty(cellData.value.quantity)
        }
        columnContentSum.setCellValueFactory { cellData ->
            return@setCellValueFactory SimpleObjectProperty(cellData.value.sum)
        }
    }

    private fun showContent() {
        tableContent.items = contentList
    }

    private fun showDetails(idProvider: Int) {
        tableDetail.items = DataBaseHandler.getDetailPriceList(idProvider)
    }

    private fun updateTotal() {
        var sum = 0f
        var weight = 0f
        contentList.forEach {
            sum += it.sum
            weight += it.detail.weight * it.quantity
        }
        labelTotalSum.text = sum.toString()
        labelTotalWeight.text = weight.toString()
    }

}
