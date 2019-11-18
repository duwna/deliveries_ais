package com.duwna.controllers.tableControllers

import com.duwna.database.DataBaseHandler
import com.duwna.database.PASSWORD
import com.duwna.database.URL
import com.duwna.database.USER
import com.duwna.models.User
import javafx.fxml.Initializable
import java.net.URL
import java.util.*
import javafx.fxml.FXML
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory

class TableUserController : Initializable {

    @FXML
    lateinit var tableUser: TableView<User>
    @FXML
    lateinit var columnId: TableColumn<User, Int>
    @FXML
    lateinit var columnLogin: TableColumn<User, String>
    @FXML
    lateinit var columnPosition: TableColumn<User, Short>
    @FXML
    lateinit var columnFirstName: TableColumn<User, String>
    @FXML
    lateinit var columnLastName: TableColumn<User, String>
    @FXML
    lateinit var columnPhone: TableColumn<User, Long>

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        DataBaseHandler.makeConnection(URL, USER, PASSWORD)

        showUsers()

        tableUser.setOnMouseClicked {
           println(tableUser.selectionModel.selectedItem)
        }

    }

    private fun showUsers() {
        columnId.cellValueFactory = PropertyValueFactory(User.COLUMN_ID_USER)
        columnLogin.cellValueFactory = PropertyValueFactory(User.COLUMN_LOGIN)
        columnPosition.cellValueFactory = PropertyValueFactory(User.COLUMN_POSITION)
        columnFirstName.cellValueFactory = PropertyValueFactory(User.COLUMN_FIRST_NAME)
        columnLastName.cellValueFactory = PropertyValueFactory(User.COLUMN_LAST_NAME)
        columnPhone.cellValueFactory = PropertyValueFactory(User.COLUMN_PHONE)

        tableUser.items = DataBaseHandler.getUserList()
    }
}