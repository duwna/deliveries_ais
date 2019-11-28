package com.duwna.models

interface BaseModel {
    fun makeInsertString(): String
    fun makeUpdateString(): String
}