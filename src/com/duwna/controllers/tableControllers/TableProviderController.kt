package com.duwna.controllers.tableControllers


import com.duwna.database.DataBaseHandler
import com.duwna.models.Provider
import com.duwna.utils.generateDetail
import com.duwna.utils.showAlert
import javafx.fxml.Initializable
import java.net.URL
import java.util.*
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import javafx.scene.control.cell.PropertyValueFactory


class TableProviderController : TableController {

    @FXML
    lateinit var tfName: TextField
    @FXML
    lateinit var tfRating: TextField
    @FXML
    lateinit var tfCity: TextField
    @FXML
    lateinit var tfAddress: TextField
    @FXML
    lateinit var btnAdd: Button
    @FXML
    lateinit var btnUpdate: Button
    @FXML
    lateinit var btnDelete: Button
    @FXML
    lateinit var tableProvider: TableView<Provider>
    @FXML
    lateinit var columnId: TableColumn<Provider, Int>
    @FXML
    lateinit var columnName: TableColumn<Provider, String>
    @FXML
    lateinit var columnRating: TableColumn<Provider, Float>
    @FXML
    lateinit var columnCity: TableColumn<Provider, String>
    @FXML
    lateinit var columnAddress: TableColumn<Provider, String>

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        initButtons()
        initTable()
        show()
    }

    override fun initTable() {
        columnId.cellValueFactory = PropertyValueFactory(Provider.COLUMN_ID_PROVIDER)
        columnName.cellValueFactory = PropertyValueFactory(Provider.COLUMN_NAME)
        columnRating.cellValueFactory = PropertyValueFactory(Provider.COLUMN_RATING)
        columnCity.cellValueFactory = PropertyValueFactory(Provider.COLUMN_CITY)
        columnAddress.cellValueFactory = PropertyValueFactory(Provider.COLUMN_ADDRESS)
    }

    override fun initButtons() {
        tableProvider.setOnMouseClicked { fillTextFields() }
        btnAdd.setOnAction { insert() }
        btnDelete.setOnAction { delete() }
        btnUpdate.setOnAction { update() }
    }

    override fun show() {
        tableProvider.items = DataBaseHandler.getProviderList()
    }

    override fun insert() {
        if (DataBaseHandler.checkProviderName(tfName.text)) {
            showAlert("Поставщик с таким именем уже существует")
            return
        } else if (validate()) {
            DataBaseHandler.insertProvider(Provider(
                    null,
                    tfName.text,
                    15f,
                    tfCity.text,
                    tfAddress.text
            ))
            show()
            tableProvider.scrollTo(tableProvider.items.size)
        }
    }

    override fun update() {
        if (tfRating.text.isBlank() || tfRating.text.toFloatOrNull() == null || tfRating.text.toFloat() > 100) {
            showAlert("Рейтинг введен некорректно")
            return
        }
        if (validate()) {
            DataBaseHandler.updateProvider(Provider(
                    tableProvider.selectionModel.selectedItem.idProvider,
                    tfName.text,
                    tfRating.text.toFloat(),
                    tfCity.text,
                    tfAddress.text
            ))
            show()
            tableProvider.scrollTo(tableProvider.items.size)
        }
    }

    override fun delete() {
        tableProvider.selectionModel.selectedItem.idProvider?.let { id ->
            DataBaseHandler.deleteProvider(id)
        }
        show()
    }

    override fun validate(): Boolean = when {
        tfName.text.isBlank() -> {
            showAlert("Имя не должно быть пустым")
            false
        }
        else -> true
    }

    override fun fillTextFields() {
        val provider = tableProvider.selectionModel.selectedItem
        if (provider != null) {
            tfName.text = provider.name
            tfRating.text = provider.rating.toString()
            tfCity.text = provider.city
            tfAddress.text = provider.address
        }
    }

}