package com.duwna.controllers

import com.duwna.models.User
import database.*
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
import javafx.scene.paint.Color
import java.net.URL
import java.sql.SQLException
import java.util.*


class EnterController : Initializable {

    @FXML
    lateinit var tf_firstName: TextField
    @FXML
    lateinit var tf_lastName: TextField
    @FXML
    lateinit var tf_phone: TextField
    @FXML
    lateinit var tf_login: TextField
    @FXML
    lateinit var tf_password: PasswordField
    @FXML
    lateinit var tf_rePassword: PasswordField
    @FXML
    lateinit var btn_reg: Button
    @FXML
    lateinit var label_current: Label
    @FXML
    lateinit var label_next: Label
    @FXML
    lateinit var rb_chief: RadioButton
    @FXML
    lateinit var rb_ec: RadioButton
    @FXML
    lateinit var rb_disp: RadioButton
    @FXML
    lateinit var label_connection: Label
    @FXML
    lateinit var tf_user: TextField
    @FXML
    lateinit var tf_pass: TextField
    @FXML
    lateinit var tf_host: TextField
    @FXML
    lateinit var tf_port: TextField
    @FXML
    lateinit var label_connect: Label

    var isRegistrationVisible = true
    var isConnectionVisible = false

    override fun initialize(location: URL?, resources: ResourceBundle?) {

        tryConnection()

        label_connect.setOnMouseClicked { tryConnection() }
        label_connection.setOnMouseClicked { changeConnectionState() }
        label_next.setOnMouseClicked { changeRegistrationState() }

        btn_reg.setOnAction {
            println(isConnectionVisible)
            when {
                isRegistrationVisible && DataBaseHandler.isConnected && validateRegistration() -> {
                    //registration
                    val user = DataBaseHandler.getUserByLogin(tf_login.text)
                    if (user == null) {
                        DataBaseHandler.insertUser(User(null,
                                tf_login.text,
                                tf_password.text,
                                getPosition(),
                                tf_firstName.text,
                                tf_lastName.text,
                                tf_phone.text.toLong()))
                        showAlert("Регистрация прошла успешно!")
                    } else
                        showAlert("Пользователь с таким логином уже существует!")
                }
                !isRegistrationVisible && DataBaseHandler.isConnected && validateEnter() -> {
                    //enter
                    val user = DataBaseHandler.getUserByLogin(tf_login.text)
                    if (user != null && user.password == tf_password.text)
                        println("Enter successful")
                    else showAlert("Неверный логин или пароль!")
                }
            }
        }
    }


    private fun changeRegistrationState() {
        if (isRegistrationVisible) {
            label_current.text = "Вход"
            label_next.text = "Зарегистрироваться"
            tf_firstName.isVisible = false
            tf_lastName.isVisible = false
            tf_phone.isVisible = false
            tf_rePassword.isVisible = false
            rb_chief.isVisible = false
            rb_ec.isVisible = false
            rb_disp.isVisible = false
            btn_reg.text = "Войти"
        } else {
            label_current.text = "Регистрация"
            label_next.text = "Войти"
            tf_firstName.isVisible = true
            tf_lastName.isVisible = true
            tf_phone.isVisible = true
            tf_rePassword.isVisible = true
            rb_chief.isVisible = true
            rb_ec.isVisible = true
            rb_disp.isVisible = true
            btn_reg.text = "Зарегистрироваться"
        }
        isRegistrationVisible = !isRegistrationVisible
    }

    private fun tryConnection() {
        try {
            val url = "$DRIVER://${tf_host.text}:${tf_port.text}/$DATABASE"
            DataBaseHandler.makeConnection(url, tf_user.text, tf_pass.text)
            DataBaseHandler.isConnected = true
            label_connection.textFill = Color.web("#116811")
        } catch (e: SQLException) {
            DataBaseHandler.isConnected = false
            label_connection.textFill = Color.web("#ff0000")
        }
    }

    private fun changeConnectionState() {
        if (isConnectionVisible) {
            label_connect.isVisible = false
            tf_port.isVisible = false
            tf_host.isVisible = false
            tf_user.isVisible = false
            tf_pass.isVisible = false
        } else {
            label_connect.isVisible = true
            tf_port.isVisible = true
            tf_host.isVisible = true
            tf_user.isVisible = true
            tf_pass.isVisible = true
        }
        isConnectionVisible = !isConnectionVisible
    }


    private fun getPosition() = when {
        rb_ec.isSelected -> EC_POSITION
        rb_disp.isSelected -> DISP_POSITION
        rb_chief.isSelected -> CHIEF_POSITION
        else -> throw Exception()
    }

    private fun validateRegistration(): Boolean {

        var isValid = true

        if (tf_firstName.text.isBlank()) {
            tf_firstName.style = styleRed
            isValid = false
        } else tf_firstName.style = styleGray

        if (tf_lastName.text.isBlank()) {
            tf_lastName.style = styleRed
            isValid = false
        } else tf_lastName.style = styleGray

        if (tf_login.text.isBlank()) {
            tf_login.style = styleRed
            isValid = false
        } else tf_login.style = styleGray

        when {
            tf_phone.text.isBlank() -> {
                tf_phone.style = styleRed
                isValid = false
            }
            tf_phone.text.toLongOrNull() == null -> {
                tf_phone.style = styleTextRed
                isValid = false
            }
            else -> {
                tf_phone.style = styleGray
                tf_phone.style = styleTextGray
            }
        }

        if (tf_password.text.isBlank()) {
            tf_password.style = styleRed
            isValid = false
        } else tf_password.style = styleGray

        when {
            tf_rePassword.text.isBlank() -> {
                tf_rePassword.style = styleRed
                isValid = false
            }
            tf_rePassword.text != tf_password.text -> {
                tf_rePassword.style = styleTextRed
                isValid = false
            }
            else -> tf_rePassword.style = styleGray

        }

        return isValid
    }

    private fun validateEnter(): Boolean {

        var isValid = true

        if (tf_login.text.isBlank()) {
            tf_login.style = styleRed
            isValid = false
        } else tf_login.style = styleGray

        if (tf_password.text.isBlank()) {
            tf_password.style = styleRed
            isValid = false
        } else tf_password.style = styleGray

        return isValid
    }

    private fun showAlert(text: String) {
        Alert(Alert.AlertType.NONE, text, ButtonType.OK).show()
    }

    companion object {
        const val styleRed = "-fx-prompt-text-fill: red;"
        const val styleGray = "-fx-prompt-text-fill: gray;"
        const val styleTextRed = "-fx-text-inner-color: red;"
        const val styleTextGray = "-fx-text-inner-color: gray;"
    }
}
