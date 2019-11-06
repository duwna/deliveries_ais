package com.duwna.models

data class User(
    val idUser: Int?,
    val login: String,
    val password: String,
    val position: Short,
    val firstName: String,
    val lastName: String,
    val phone: Long
) {
    fun makeInsertString(): String =
        "INSERT INTO User VALUES (default, '$login', '$password', $position, '$firstName', '$lastName', $phone)"
}
