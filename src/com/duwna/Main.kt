package com.duwna

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage


///home/grigorii/IdeaProjects/jdk1.8.0_231/jre/bin/java -jar /home/grigorii/IdeaProjects/DeliveryDepartment/out/artifacts/JavaFXApp/bundles/JavaFXApp/app/JavaFXApp.jar

class Main : Application() {

    @Throws(Exception::class)
    override fun start(primaryStage: Stage) {
        val root = FXMLLoader.load<Parent>(javaClass.getResource("/com/duwna/fxml/enter.fxml"))
        primaryStage.apply {
            title = "Отдел доставок"
            minWidth = 350.0
            minHeight = 600.0
            scene = Scene(root, 1024.0, 768.0)
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
