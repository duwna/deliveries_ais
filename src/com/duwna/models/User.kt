package com.duwna.models

data class User(
        val idUser: Int?,
        val login: String,
        val password: String,
        val position: Short,
        val firstName: String,
        val lastName: String,
        val phone: Long
) : BaseModel {

    override fun makeUpdateString(): String = "UPDATE User SET " +
            "$COLUMN_LOGIN = '$login'," +
            "$COLUMN_PASSWORD = '$password'," +
            "$COLUMN_POSITION = $position, " +
            "$COLUMN_FIRST_NAME = '$firstName', " +
            "$COLUMN_LAST_NAME = '$lastName', " +
            "$COLUMN_PHONE = $phone " +
            "WHERE $COLUMN_ID_USER = $idUser"

    override fun makeInsertString(): String =
            "INSERT INTO User VALUES (default, '$login', '$password', $position, '$firstName', '$lastName', $phone)"

    companion object {
        const val COLUMN_ID_USER = "idUser"
        const val COLUMN_LOGIN = "login"
        const val COLUMN_PASSWORD = "password"
        const val COLUMN_POSITION = "position"
        const val COLUMN_FIRST_NAME = "firstName"
        const val COLUMN_LAST_NAME = "lastName"
        const val COLUMN_PHONE = "phone"
    }
}
