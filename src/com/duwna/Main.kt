package com.duwna

import com.duwna.database.DataBaseHandler
import com.duwna.database.PASSWORD
import com.duwna.database.URL
import com.duwna.database.USER
import com.duwna.models.Order
import com.duwna.utils.sqlFormat
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import java.util.*

///home/grigorii/IdeaProjects/jdk1.8.0_231/jre/bin/java -jar /home/grigorii/IdeaProjects/DeliveryDepartment/out/artifacts/JavaFXApp/bundles/JavaFXApp/app/JavaFXApp.jar

class Main : Application() {

    @Throws(Exception::class)
    override fun start(primaryStage: Stage) {
        DataBaseHandler.makeConnection(URL, USER, PASSWORD)
//        val root = FXMLLoader.load<Parent>(javaClass.getResource("/com/duwna/fxml/enter.fxml"))
//        with(primaryStage) {
//            title = "Отдел доставок"
//            minWidth = 350.0
//            minHeight = 600.0
//            primaryStage.scene = Scene(root, 1024.0, 768.0)
//            primaryStage.show()
//        }
//        val detail = FXMLLoader.load<Parent>(javaClass.getResource("/com/duwna/fxml/tables/table_detail.fxml"))
//        Stage().apply{
//            title = ""
//            minWidth = 350.0
//            minHeight = 600.0
//            scene = Scene(detail, 1024.0, 768.0)
//            show()
//        }
//        val provider = FXMLLoader.load<Parent>(javaClass.getResource("/com/duwna/fxml/tables/table_provider.fxml"))
//        Stage().apply{
//            title = ""
//            minWidth = 350.0
//            minHeight = 600.0
//            scene = Scene(provider, 1024.0, 768.0)
//            show()
//        }
//        val detailProvider = FXMLLoader.load<Parent>(javaClass.getResource("/com/duwna/fxml/tables/table_detail_provider.fxml"))
//        Stage().apply{
//            title = ""
//            minWidth = 350.0
//            minHeight = 600.0
//            scene = Scene(detailProvider, 1024.0, 768.0)
//            show()
//        }
////
//        val order = FXMLLoader.load<Parent>(javaClass.getResource("/com/duwna/fxml/order.fxml"))
//        Stage().apply{
//            title = ""
//            minWidth = 350.0
//            minHeight = 600.0
//            scene = Scene(order, 1024.0, 768.0)
//            show()
//        }
        val detailProvider = FXMLLoader.load<Parent>(javaClass.getResource("/com/duwna/fxml/detail_provider.fxml"))
        Stage().apply{
            title = ""
            minWidth = 350.0
            minHeight = 600.0
            scene = Scene(detailProvider, 1024.0, 768.0)
            show()
        }
    }


    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(Main::class.java)
        }
    }
}
