package com.duwna.models

data class Provider(
        val idProvider: Int?,
        val name: String,
        val rating: Float,
        val city: String,
        val address: String
) {
    fun makeInsertString(): String =
            "INSERT INTO Provider VALUES (default, '$name', $rating, '$city', '$address')"

    fun makeUpdateString(): String = "UPDATE Provider SET " +
            "$COLUMN_NAME = '$name'," +
            "$COLUMN_RATING = $rating," +
            "$COLUMN_CITY = '$city'," +
            "$COLUMN_ADDRESS = '$address' " +
            "WHERE $COLUMN_ID_PROVIDER = $idProvider"


    companion object {
        const val COLUMN_ID_PROVIDER = "idProvider"
        const val COLUMN_NAME = "name"
        const val COLUMN_RATING = "rating"
        const val COLUMN_CITY = "city"
        const val COLUMN_ADDRESS = "address"
    }
}