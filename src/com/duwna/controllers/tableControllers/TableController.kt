package com.duwna.controllers.tableControllers

import javafx.fxml.Initializable

interface TableController : Initializable {
    fun show()
    fun insert()
    fun update()
    fun delete()
    fun validate(): Boolean
    fun fillTextFields()
    fun initTable()
    fun initButtons()
}