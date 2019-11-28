package com.duwna.utils

import com.duwna.database.DATE_FORMAT
import com.duwna.database.DATE_TIME_FORMAT
import com.duwna.models.User
import javafx.fxml.FXMLLoader
import javafx.fxml.FXMLLoader.*
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.stage.Stage
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

fun showAlert(text: String) {
    Alert(Alert.AlertType.NONE, text, ButtonType.OK).show()
}

fun Date.sqlDateTime(): String {
    val dateFormat = SimpleDateFormat(DATE_TIME_FORMAT, Locale("ru"))
    return dateFormat.format(this)
}

fun getDayDifference(date1: String, date2: String): Int {
    val dateFormat = SimpleDateFormat(DATE_FORMAT)
    val diff = dateFormat.parse(date2).time - dateFormat.parse(date1).time
    return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS).toInt()
}
