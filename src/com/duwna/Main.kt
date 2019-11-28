package com.duwna

import com.duwna.database.*
import com.duwna.utils.getDayDifference
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


///home/grigorii/IdeaProjects/jdk1.8.0_231/jre/bin/java -jar /home/grigorii/IdeaProjects/DeliveryDepartment/out/artifacts/JavaFXApp/bundles/JavaFXApp/app/JavaFXApp.jar

class Main : Application() {

    @Throws(Exception::class)
    override fun start(primaryStage: Stage) {
        //DataBaseHandler.makeConnection(URL, USER, PASSWORD)
        val root = FXMLLoader.load<Parent>(javaClass.getResource("/com/duwna/fxml/enter.fxml"))
        primaryStage.apply {
            title = "Отдел доставок"
            minWidth = 350.0
            minHeight = 600.0
            scene = Scene(root, 1024.0, 768.0)
            show()
        }

//        val order = FXMLLoader.load<Parent>(javaClass.getResource("/com/duwna/fxml/order.fxml"))
//        Stage().apply{
//            title = ""
//            minWidth = 350.0
//            minHeight = 600.0
//            scene = Scene(order, 1024.0, 768.0)
//            show()
//        }
//        val detailProvider = FXMLLoader.load<Parent>(javaClass.getResource("/com/duwna/fxml/detail_provider.fxml"))
//        Stage().apply{
//            title = ""
//            minWidth = 350.0
//            minHeight = 600.0
//            scene = Scene(detailProvider, 1024.0, 768.0)
//            show()
//        }
//        val delivery = FXMLLoader.load<Parent>(javaClass.getResource("/com/duwna/fxml/delivery.fxml"))
//        Stage().apply{
//            title = ""
//            minWidth = 350.0
//            minHeight = 600.0
//            scene = Scene(delivery, 1024.0, 768.0)
//            show()
//        }
    }


    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(Main::class.java)
        }
    }
}
