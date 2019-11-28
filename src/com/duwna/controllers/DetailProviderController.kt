package com.duwna.controllers

import com.duwna.database.DataBaseHandler
import com.duwna.models.Detail
import com.duwna.models.DetailProvider
import com.duwna.models.Provider
import com.duwna.utils.showAlert
import javafx.beans.property.SimpleObjectProperty
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import java.net.URL
import java.sql.SQLIntegrityConstraintViolationException
import java.util.*


class DetailProviderController : Initializable {

    @FXML
    lateinit var tfProviderName: TextField
    @FXML
    lateinit var tfRating: TextField
    @FXML
    lateinit var tfCity: TextField
    @FXML
    lateinit var tfAddress: TextField
    @FXML
    lateinit var btnProviderAdd: Button
    @FXML
    lateinit var btnProviderUpdate: Button
    @FXML
    lateinit var btnProviderDelete: Button
    @FXML
    lateinit var tfDetailName: TextField
    @FXML
    lateinit var tfWeight: TextField
    @FXML
    lateinit var cbIsHighTech: CheckBox
    @FXML
    lateinit var btnDetailAdd: Button
    @FXML
    lateinit var btnDetailUpdate: Button
    @FXML
    lateinit var btnDetailDelete: Button
    @FXML
    lateinit var tableProvider: TableView<Provider>
    @FXML
    lateinit var columnProviderName: TableColumn<Provider, String>
    @FXML
    lateinit var columnRating: TableColumn<Provider, Float>
    @FXML
    lateinit var columnCity: TableColumn<Provider, String>
    @FXML
    lateinit var columnAddress: TableColumn<Provider, String>
    @FXML
    lateinit var tableDetail: TableView<Detail>
    @FXML
    lateinit var columnDetailName: TableColumn<Detail, String>
    @FXML
    lateinit var columnWeight: TableColumn<Detail, Float>
    @FXML
    lateinit var columnIsHighTech: TableColumn<Detail, String>
    @FXML
    lateinit var tablePrices: TableView<Pair<Detail, DetailProvider>>
    @FXML
    lateinit var columnPricesDetail: TableColumn<Pair<Detail, DetailProvider>, String>
    @FXML
    lateinit var columnPricesPrice: TableColumn<Pair<Detail, DetailProvider>, Float>
    @FXML
    lateinit var labelProvider: Label
    @FXML
    lateinit var labelDetail: Label
    @FXML
    lateinit var tfPrice: TextField
    @FXML
    lateinit var btnPriceAdd: Button
    @FXML
    lateinit var btnPriceDelete: Button


    override fun initialize(location: URL?, resources: ResourceBundle?) {
        initProviderTable()
        initDetailTable()
        initPricesTable()
        showProviders()
        showDetails()

        tableProvider.setOnMouseClicked {
            val provider = tableProvider.selectionModel.selectedItem
            if (provider != null) {
                tfProviderName.text = provider.name
                tfRating.text = provider.rating.toString()
                tfCity.text = provider.city
                tfAddress.text = provider.address
                showPrices(provider.idProvider!!)
                labelProvider.text = provider.name
            }
        }

        tableDetail.setOnMouseClicked {
            val detail = tableDetail.selectionModel.selectedItem
            if (detail != null) {
                tfDetailName.text = detail.name
                tfWeight.text = detail.weight.toString()
                cbIsHighTech.isSelected = detail.isHighTech
                labelDetail.text = detail.name
            }
        }

        btnDetailAdd.setOnAction { insertDetail() }
        btnDetailDelete.setOnAction { deleteDetail() }
        btnDetailUpdate.setOnAction { updateDetail() }

        btnProviderAdd.setOnAction { insertProvider() }
        btnProviderDelete.setOnAction { deleteProvider() }
        btnProviderUpdate.setOnAction { updateProvider() }

        btnPriceAdd.setOnAction { insertDetailProvider() }
        btnPriceDelete.setOnAction { deleteDetailProvider() }
    }

    private fun initPricesTable() {
        columnPricesDetail.setCellValueFactory { cellData ->
            return@setCellValueFactory SimpleObjectProperty(cellData.value.first.name)
        }
        columnPricesPrice.setCellValueFactory { cellData ->
            return@setCellValueFactory SimpleObjectProperty(cellData.value.second.price)
        }
    }

    private fun showPrices(idProvider: Int) {
        tablePrices.items = DataBaseHandler.getDetailPriceList(idProvider)
    }

    private fun showDetails() {
        tableDetail.items = DataBaseHandler.getDetailList()
    }

    private fun showProviders() {
        tableProvider.items = DataBaseHandler.getProviderList()
    }

    private fun initDetailTable() {
        columnDetailName.cellValueFactory = PropertyValueFactory(Detail.COLUMN_NAME)
        columnWeight.cellValueFactory = PropertyValueFactory(Detail.COLUMN_WEIGHT)
        columnIsHighTech.setCellValueFactory { cellData ->
            return@setCellValueFactory SimpleObjectProperty(if (cellData.value.isHighTech) "Да" else "Нет")
        }
    }

    private fun initProviderTable() {
        columnProviderName.cellValueFactory = PropertyValueFactory(Provider.COLUMN_NAME)
        columnRating.cellValueFactory = PropertyValueFactory(Provider.COLUMN_RATING)
        columnCity.cellValueFactory = PropertyValueFactory(Provider.COLUMN_CITY)
        columnAddress.cellValueFactory = PropertyValueFactory(Provider.COLUMN_ADDRESS)
    }

    private fun insertDetailProvider() {
        DataBaseHandler.insertDetailProvider(DetailProvider(
                null,
                tableDetail.selectionModel.selectedItem.idDetail!!,
                tableProvider.selectionModel.selectedItem.idProvider!!,
                tfPrice.text.toFloat()
        ))
        showPrices(tableProvider.selectionModel.selectedItem.idProvider!!)
    }

    private fun deleteDetailProvider() {
        try {
            val pair = tablePrices.selectionModel.selectedItem
            if (pair != null) {
                DataBaseHandler.deleteDetailProvider(pair.second.idDetailProvider!!)
            }
            showPrices(pair.second.idProvider)
        } catch (e: SQLIntegrityConstraintViolationException) {
            showAlert("Невозможно удалить заказанную деталь.")
        }
    }

    private fun insertDetail() {
        if (DataBaseHandler.checkDetailName(tfDetailName.text)) {
            showAlert("Деталь с таким именем уже существует")
            return
        } else if (validateDetail()) {
            DataBaseHandler.insertDetail(Detail(
                    null,
                    tfDetailName.text,
                    tfWeight.text.toFloat(),
                    cbIsHighTech.isSelected
            ))
            showDetails()
            tableDetail.scrollTo(tableDetail.items.size)
        }
    }

    private fun validateDetail(): Boolean = when {
        tfDetailName.text.isBlank() -> {
            showAlert("Имя не должно быть пустым")
            false
        }
        tfWeight.text.toFloatOrNull() == null -> {
            showAlert("Вес введен некорректно")
            false
        }
        else -> true
    }

    private fun updateDetail() {
        if (validateDetail()) {
            DataBaseHandler.updateDetail(Detail(
                    tableDetail.selectionModel.selectedItem.idDetail,
                    tfDetailName.text,
                    tfWeight.text.toFloat(),
                    cbIsHighTech.isSelected
            ))
            showDetails()
        }
    }

    private fun deleteDetail() {
        try {
            tableDetail.selectionModel.selectedItem.idDetail?.let { DataBaseHandler.deleteDetail(it) }
            showDetails()
        } catch (e: SQLIntegrityConstraintViolationException) {
            showAlert("Сначала нужно удалить цены.")
        }
    }

    private fun validateProvider(): Boolean = when {
        tfProviderName.text.isBlank() -> {
            showAlert("Имя не должно быть пустым")
            false
        }
        else -> true
    }

    private fun deleteProvider() {
        try {
            tableProvider.selectionModel.selectedItem.idProvider?.let { DataBaseHandler.deleteProvider(it) }
            showProviders()
        } catch (e: SQLIntegrityConstraintViolationException) {
            showAlert("Сначала нужно удалить цены.")
        }
    }

    private fun updateProvider() {
        if (tfRating.text.isBlank() || tfRating.text.toFloatOrNull() == null || tfRating.text.toFloat() > 100) {
            showAlert("Рейтинг введен некорректно")
            return
        }
        if (validateProvider()) {
            DataBaseHandler.updateProvider(Provider(
                    tableProvider.selectionModel.selectedItem.idProvider,
                    tfProviderName.text,
                    tfRating.text.toFloat(),
                    tfCity.text,
                    tfAddress.text
            ))
            showProviders()
            tableProvider.scrollTo(tableProvider.items.size)
        }
    }

    private fun insertProvider() {
        if (DataBaseHandler.checkProviderName(tfProviderName.text)) {
            showAlert("Поставщик с таким именем уже существует")
            return
        } else if (validateProvider()) {
            DataBaseHandler.insertProvider(Provider(
                    null,
                    tfProviderName.text,
                    15f,
                    tfCity.text,
                    tfAddress.text
            ))
            showProviders()
            tableProvider.scrollTo(tableProvider.items.size)
        }
    }

}