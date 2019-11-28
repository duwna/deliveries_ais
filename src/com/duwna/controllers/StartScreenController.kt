package com.duwna.controllers

import com.duwna.models.User
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Label
import java.net.URL
import java.util.*

class StartScreenController : Initializable {

    @FXML
    lateinit var labelFullName: Label
    lateinit var user: User

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        labelFullName.text = "${user.firstName} ${user.lastName}"
    }

    fun initUser(user: User){
        this.user = user
    }
}