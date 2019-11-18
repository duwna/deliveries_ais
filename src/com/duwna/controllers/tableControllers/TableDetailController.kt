package com.duwna.controllers.tableControllers

import com.duwna.database.DataBaseHandler
import com.duwna.models.Detail
import com.duwna.utils.showAlert
import javafx.beans.property.ReadOnlyStringWrapper
import java.net.URL
import java.util.*
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory


class TableDetailController : TableController {

    @FXML
    private lateinit var tfName: TextField
    @FXML
    private lateinit var tfWeight: TextField
    @FXML
    private lateinit var cbIsHighTech: CheckBox
    @FXML
    private lateinit var btnAdd: Button
    @FXML
    private lateinit var btnUpdate: Button
    @FXML
    private lateinit var btnDelete: Button
    @FXML
    private lateinit var tableDetail: TableView<Detail>
    @FXML
    private lateinit var columnId: TableColumn<Detail, Int>
    @FXML
    private lateinit var columnName: TableColumn<Detail, String>
    @FXML
    private lateinit var columnWeight: TableColumn<Detail, Float>
    @FXML
    private lateinit var columnIsHighTech: TableColumn<Detail, String>


    override fun initialize(location: URL?, resources: ResourceBundle?) {
        initTable()
        initButtons()
        show()
    }

    override fun initTable() {
        columnId.cellValueFactory = PropertyValueFactory(Detail.COLUMN_ID_DETAIL)
        columnName.cellValueFactory = PropertyValueFactory(Detail.COLUMN_NAME)
        columnWeight.cellValueFactory = PropertyValueFactory(Detail.COLUMN_WEIGHT)
        columnIsHighTech.setCellValueFactory { cellData ->
            return@setCellValueFactory ReadOnlyStringWrapper(
                    if (cellData.value.isHighTech) "Да" else "Нет"
            )
        }
    }

    override fun initButtons() {
        tableDetail.setOnMouseClicked { fillTextFields() }
        btnAdd.setOnAction { insert() }
        btnDelete.setOnAction { delete() }
        btnUpdate.setOnAction { update() }
    }

    override fun fillTextFields() {
        val detail = tableDetail.selectionModel.selectedItem
        if (detail != null) {
            tfName.text = detail.name
            tfWeight.text = detail.weight.toString()
            cbIsHighTech.isSelected = detail.isHighTech
        }
    }

    override fun show() {
        tableDetail.items = DataBaseHandler.getDetailList()
    }

    override fun insert() {
        if (DataBaseHandler.checkDetailName(tfName.text)) {
            showAlert("Деталь с таким именем уже существует")
            return
        } else if (validate()) {
            DataBaseHandler.insertDetail(Detail(
                    null,
                    tfName.text,
                    tfWeight.text.toFloat(),
                    cbIsHighTech.isSelected
            ))
            show()
            tableDetail.scrollTo(tableDetail.items.size)
        }
    }

    override fun delete() {
        tableDetail.selectionModel.selectedItem.idDetail?.let { DataBaseHandler.deleteDetail(it) }
        show()
    }

    override fun update() {
        if (validate()) {
            DataBaseHandler.updateDetail(Detail(
                    tableDetail.selectionModel.selectedItem.idDetail,
                    tfName.text,
                    tfWeight.text.toFloat(),
                    cbIsHighTech.isSelected
            ))
            show()
        }
    }

    override fun validate(): Boolean = when {
        tfName.text.isBlank() -> {
            showAlert("Имя не должно быть пустым")
            false
        }
        tfWeight.text.toFloatOrNull() == null -> {
            showAlert("Вес введен некорректно")
            false
        }
        else -> true
    }
}