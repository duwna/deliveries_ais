package com.duwna.utils

import javafx.scene.control.Alert
import javafx.scene.control.ButtonType

fun showAlert(text: String) {
    Alert(Alert.AlertType.NONE, text, ButtonType.OK).show()
}