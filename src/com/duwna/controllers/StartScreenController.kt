package com.duwna.controllers

import com.duwna.database.CHIEF_POSITION
import com.duwna.database.DISP_POSITION
import com.duwna.database.DataBaseHandler
import com.duwna.database.EC_POSITION
import com.duwna.models.User
import com.duwna.utils.getPositionName
import com.duwna.utils.openNextWindow
import com.duwna.utils.showAlert
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.PasswordField
import javafx.scene.control.TextField
import javafx.scene.layout.VBox
import java.net.URL
import java.util.*


class StartScreenController : Initializable {

    @FXML
    lateinit var labelFullName: Label
    @FXML
    lateinit var labelPosition: Label
    @FXML
    lateinit var labelDetails: Label
    @FXML
    lateinit var labelDeliveries: Label
    @FXML
    lateinit var labelOrder: Label
    @FXML
    lateinit var labelEdit: Label
    @FXML
    lateinit var labelExit: Label
    @FXML
    lateinit var labelUsers: Label
    @FXML
    lateinit var vboxEdit: VBox
    @FXML
    lateinit var tfFirstName: TextField
    @FXML
    lateinit var tfLastName: TextField
    @FXML
    lateinit var tfPhone: TextField
    @FXML
    lateinit var tfLogin: TextField
    @FXML
    lateinit var tfPassword: PasswordField
    @FXML
    lateinit var tfRePassword: PasswordField
    @FXML
    lateinit var btnSave: Button

    private var user = DataBaseHandler.currentUser
    private var isEdit = false

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        initViews()

        labelDetails.setOnMouseClicked {
            openNextWindow(javaClass.getResource("/com/duwna/fxml/detail_provider.fxml"),
                    "Детали и поставщики")
        }
        labelOrder.setOnMouseClicked {
            openNextWindow(javaClass.getResource("/com/duwna/fxml/order.fxml"),
                    "Заказ деталей")
        }
        labelDeliveries.setOnMouseClicked {
            openNextWindow(javaClass.getResource("/com/duwna/fxml/delivery.fxml"),
                    "Заказы и неустойка")
        }
        labelUsers.setOnMouseClicked {
            openNextWindow(javaClass.getResource("/com/duwna/fxml/table_user.fxml"),
                    "Пользователи")
        }
        labelEdit.setOnMouseClicked { changeEdit() }
        labelExit.setOnMouseClicked { event ->
            openNextWindow(javaClass.getResource("/com/duwna/fxml/enter.fxml"), "Отдел доставок")
            (event.source as Node).scene.window.hide()
        }
        btnSave.setOnAction {
            if (validateUpdate()) {
                val newUser = User(
                        user.idUser,
                        tfLogin.text,
                        tfPassword.text,
                        user.position,
                        tfFirstName.text,
                        tfLastName.text,
                        tfPhone.text.toLong()
                )

                DataBaseHandler.updateUser(newUser)
                user = newUser
                showAlert("Сохранено.")
                initViews()
            }
        }

    }

    private fun initViews() {

        labelDeliveries.isVisible = user.position == CHIEF_POSITION || user.position == DISP_POSITION
        labelOrder.isVisible = user.position == EC_POSITION
        labelUsers.isVisible = user.position == CHIEF_POSITION

        labelFullName.text = "${user.firstName} ${user.lastName}"
        labelPosition.text = getPositionName(user.position)

        tfFirstName.text = user.firstName
        tfLastName.text = user.lastName
        tfLogin.text = user.login
        tfPhone.text = user.phone.toString()
        tfPassword.text = user.password
        tfRePassword.text = user.password
    }

    private fun changeEdit() {
        isEdit = !isEdit
        vboxEdit.isVisible = isEdit
    }

    private fun validateUpdate(): Boolean {

        var isValid = true

        if (tfFirstName.text.isBlank()) {
            tfFirstName.style = EnterController.styleHintRed
            isValid = false
        } else tfFirstName.style = EnterController.styleHintGray

        if (tfLastName.text.isBlank()) {
            tfLastName.style = EnterController.styleHintRed
            isValid = false
        } else tfLastName.style = EnterController.styleHintGray

        if (tfLogin.text.isBlank()) {
            tfLogin.style = EnterController.styleHintRed
            isValid = false
        } else tfLogin.style = EnterController.styleHintGray

        when {
            tfPhone.text.isBlank() -> {
                tfPhone.style = EnterController.styleHintRed
                isValid = false
            }
            tfPhone.text.toLongOrNull() == null -> {
                tfPhone.style = EnterController.styleTextRed
                isValid = false
            }
            else -> {
                tfPhone.style = EnterController.styleHintGray
                tfPhone.style = EnterController.styleTextBlack
            }
        }

        if (tfPassword.text.isBlank()) {
            tfPassword.style = EnterController.styleHintRed
            isValid = false
        } else tfPassword.style = EnterController.styleHintGray

        when {
            tfRePassword.text.isBlank() -> {
                tfRePassword.style = EnterController.styleHintRed
                isValid = false
            }
            tfRePassword.text != tfPassword.text -> {
                tfRePassword.style = EnterController.styleTextRed
                isValid = false
            }
            else -> tfRePassword.style = EnterController.styleHintGray
        }
        return isValid
    }

}