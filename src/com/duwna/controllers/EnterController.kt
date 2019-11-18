package com.duwna.controllers

import com.duwna.models.User
import com.duwna.database.*
import com.duwna.utils.showAlert
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import java.net.URL
import java.sql.SQLException
import java.util.*


class EnterController : Initializable {

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
    lateinit var btnReg: Button
    @FXML
    lateinit var labelCurrent: Label
    @FXML
    lateinit var labelNext: Label
    @FXML
    lateinit var rbChief: RadioButton
    @FXML
    lateinit var rbEc: RadioButton
    @FXML
    lateinit var rbDisp: RadioButton
    @FXML
    lateinit var labelConnection: Label
    @FXML
    lateinit var tfUser: TextField
    @FXML
    lateinit var tfPass: TextField
    @FXML
    lateinit var tfHost: TextField
    @FXML
    lateinit var tfPort: TextField
    @FXML
    lateinit var labelConnect: Label
    @FXML
    lateinit var vbox: VBox

    private var isRegistrationVisible = true
    private var isConnectionVisible = false

    override fun initialize(location: URL?, resources: ResourceBundle?) {

        initConnectionFields()
        tryConnection()

        labelConnect.setOnMouseClicked { tryConnection() }
        labelConnection.setOnMouseClicked { changeConnectionState() }
        labelNext.setOnMouseClicked { changeRegistrationState() }

        btnReg.setOnAction {
            if (isRegistrationVisible && DataBaseHandler.isConnected && validateRegistration())
                registration()
            else if (!isRegistrationVisible && DataBaseHandler.isConnected && validateEnter())
                enter()
        }
    }

    private fun registration() {
        val user = DataBaseHandler.getUserByLogin(tfLogin.text)
        if (user == null) {
            DataBaseHandler.insertUser(User(null,
                    tfLogin.text,
                    tfPassword.text,
                    getPosition(),
                    tfFirstName.text,
                    tfLastName.text,
                    tfPhone.text.toLong()))
            showAlert("Регистрация прошла успешно!")
        } else
            showAlert("Пользователь с таким логином уже существует!")
    }

    private fun enter() {
        val user = DataBaseHandler.getUserByLogin(tfLogin.text)
        if (user != null && user.password == tfPassword.text)
            openNextWindow()
        else showAlert("Неверный логин или пароль!")
    }

    private fun openNextWindow() {

    }

    private fun changeRegistrationState() {
        if (isRegistrationVisible) {
            labelCurrent.text = "Вход"
            labelNext.text = "Зарегистрироваться"
            tfFirstName.isVisible = false
            tfLastName.isVisible = false
            tfPhone.isVisible = false
            tfRePassword.isVisible = false
            rbChief.isVisible = false
            rbEc.isVisible = false
            rbDisp.isVisible = false
            btnReg.text = "Войти"
        } else {
            labelCurrent.text = "Регистрация"
            labelNext.text = "Войти"
            tfFirstName.isVisible = true
            tfLastName.isVisible = true
            tfPhone.isVisible = true
            tfRePassword.isVisible = true
            rbChief.isVisible = true
            rbEc.isVisible = true
            rbDisp.isVisible = true
            btnReg.text = "Зарегистрироваться"
        }
        isRegistrationVisible = !isRegistrationVisible
    }

    private fun tryConnection() {
        try {
            val url = "$DRIVER://${tfHost.text}:${tfPort.text}/$DATABASE"
            DataBaseHandler.makeConnection(url, tfUser.text, tfPass.text)
            DataBaseHandler.isConnected = true
            labelConnection.textFill = Color.web("#116811")
        } catch (e: SQLException) {
            DataBaseHandler.isConnected = false
            labelConnection.textFill = Color.web("#ff0000")
        }
    }

    private fun initConnectionFields() {
        tfUser.text = USER
        tfHost.text = HOST
        tfPass.text = PASSWORD
        tfPort.text = PORT
    }

    private fun changeConnectionState() {
        if (isConnectionVisible) {
            labelConnect.isVisible = false
            tfPort.isVisible = false
            tfHost.isVisible = false
            tfUser.isVisible = false
            tfPass.isVisible = false
        } else {
            labelConnect.isVisible = true
            tfPort.isVisible = true
            tfHost.isVisible = true
            tfUser.isVisible = true
            tfPass.isVisible = true
        }
        isConnectionVisible = !isConnectionVisible
    }

    private fun getPosition() = when {
        rbEc.isSelected -> EC_POSITION
        rbDisp.isSelected -> DISP_POSITION
        rbChief.isSelected -> CHIEF_POSITION
        else -> throw Exception()
    }

    private fun validateRegistration(): Boolean {

        var isValid = true

        if (tfFirstName.text.isBlank()) {
            tfFirstName.style = styleHintRed
            isValid = false
        } else tfFirstName.style = styleHintGray

        if (tfLastName.text.isBlank()) {
            tfLastName.style = styleHintRed
            isValid = false
        } else tfLastName.style = styleHintGray

        if (tfLogin.text.isBlank()) {
            tfLogin.style = styleHintRed
            isValid = false
        } else tfLogin.style = styleHintGray

        when {
            tfPhone.text.isBlank() -> {
                tfPhone.style = styleHintRed
                isValid = false
            }
            tfPhone.text.toLongOrNull() == null -> {
                tfPhone.style = styleTextRed
                isValid = false
            }
            else -> {
                tfPhone.style = styleHintGray
                tfPhone.style = styleTextBlack
            }
        }

        if (tfPassword.text.isBlank()) {
            tfPassword.style = styleHintRed
            isValid = false
        } else tfPassword.style = styleHintGray

        when {
            tfRePassword.text.isBlank() -> {
                tfRePassword.style = styleHintRed
                isValid = false
            }
            tfRePassword.text != tfPassword.text -> {
                tfRePassword.style = styleTextRed
                isValid = false
            }
            else -> tfRePassword.style = styleHintGray

        }

        return isValid
    }

    private fun validateEnter(): Boolean {

        var isValid = true

        if (tfLogin.text.isBlank()) {
            tfLogin.style = styleHintRed
            isValid = false
        } else tfLogin.style = styleHintGray

        if (tfPassword.text.isBlank()) {
            tfPassword.style = styleHintRed
            isValid = false
        } else tfPassword.style = styleHintGray

        return isValid
    }

    companion object {
        const val styleHintRed = "-fx-prompt-text-fill: red;"
        const val styleHintGray = "-fx-prompt-text-fill: gray;"
        const val styleTextRed = "-fx-text-inner-color: red;"
        const val styleTextBlack = "-fx-text-inner-color: black;"
    }
}
