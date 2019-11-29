package com.duwna.utils

import com.duwna.database.*
import javafx.fxml.FXMLLoader.*
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.stage.Stage
import java.net.URL
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
fun openNextWindow(location: URL, titleName: String) {
    val root = load<Parent>(location)
    Stage().apply {
        title = titleName
        minWidth = 350.0
        minHeight = 600.0
        scene = Scene(root, 1024.0, 768.0)
        show()
    }
}
fun getPositionName(position: Short): String = when(position) {
    CHIEF_POSITION -> "Начальник"
    DISP_POSITION -> "Диспетчер"
    EC_POSITION -> "Экономист"
    else -> throw IllegalArgumentException()
}
