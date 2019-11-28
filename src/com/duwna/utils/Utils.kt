package com.duwna.utils

import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import java.text.SimpleDateFormat
import java.util.*

fun showAlert(text: String) {
    Alert(Alert.AlertType.NONE, text, ButtonType.OK).show()
}

fun Date.sqlFormat(pattern: String = "YYYY-MM-dd HH:mm:ss"): String {
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}