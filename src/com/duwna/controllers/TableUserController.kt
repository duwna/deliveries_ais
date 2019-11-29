package com.duwna.controllers

import com.duwna.database.DataBaseHandler
import com.duwna.database.PASSWORD
import com.duwna.database.URL
import com.duwna.database.USER
import com.duwna.models.User
import com.duwna.utils.getPositionName
import javafx.beans.property.SimpleObjectProperty
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory
import java.net.URL
import java.util.*


class TableUserController : Initializable {

    @FXML
    lateinit var tableUser: TableView<User>
    @FXML
    lateinit var columnId: TableColumn<User, Int>
    @FXML
    lateinit var columnLogin: TableColumn<User, String>
    @FXML
    lateinit var columnPosition: TableColumn<User, String>
    @FXML
    lateinit var columnFirstName: TableColumn<User, String>
    @FXML
    lateinit var columnLastName: TableColumn<User, String>
    @FXML
    lateinit var columnPhone: TableColumn<User, Long>
    @FXML
    lateinit var labelId: Label
    @FXML
    lateinit var btnDelete: Button

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        DataBaseHandler.makeConnection(URL, USER, PASSWORD)

        initTable()
        showUsers()

        tableUser.setOnMouseClicked {
            val user = tableUser.selectionModel.selectedItem
            if (user != null)
                labelId.text = user.idUser.toString()
        }

        btnDelete.setOnAction {
            val user = tableUser.selectionModel.selectedItem
            if (user != null) {
                DataBaseHandler.deleteUser(user.idUser!!)
                showUsers()
            }
        }

    }

    private fun initTable() {
        columnId.cellValueFactory = PropertyValueFactory(User.COLUMN_ID_USER)
        columnLogin.cellValueFactory = PropertyValueFactory(User.COLUMN_LOGIN)
        columnFirstName.cellValueFactory = PropertyValueFactory(User.COLUMN_FIRST_NAME)
        columnLastName.cellValueFactory = PropertyValueFactory(User.COLUMN_LAST_NAME)
        columnPhone.cellValueFactory = PropertyValueFactory(User.COLUMN_PHONE)
        columnPosition.setCellValueFactory { cellData ->
            return@setCellValueFactory SimpleObjectProperty(getPositionName(cellData.value.position))
        }
    }

    private fun showUsers() {
        tableUser.items = DataBaseHandler.getUserList()
    }
}