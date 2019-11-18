package com.duwna.models

data class Detail(
        val idDetail: Int?,
        val name: String,
        val weight: Float,
        val isHighTech: Boolean
) {
    fun makeInsertString(): String =
            "INSERT INTO Detail VALUES (default, '$name', $weight, $isHighTech)"

    fun makeUpdateString(): String {
        return "UPDATE Detail SET $COLUMN_NAME = '$name', $COLUMN_WEIGHT = $weight, $COLUMN_IS_HIGH_TECH = $isHighTech " +
                "WHERE $COLUMN_ID_DETAIL = $idDetail"
    }


    companion object {
        const val COLUMN_ID_DETAIL = "idDetail"
        const val COLUMN_NAME = "name"
        const val COLUMN_WEIGHT = "weight"
        const val COLUMN_IS_HIGH_TECH = "isHighTech"
    }
}