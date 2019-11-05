package com.duwna.controllers

import javafx.fxml.FXML
import javafx.scene.control.*


class EnterController {

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

    private var isRegistration = false

    fun initialize() {

        label_next.setOnMouseClicked {
            changeRegistration()
        }

        btn_reg.setOnAction {
            if (isRegistration) {

            } else {

            }
        }
    }

    private fun changeRegistration() {
        if (isRegistration) {
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
        } else {
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
        }

        isRegistration = !isRegistration
    }
}
