package com.duwna.models

data class DetailProvider(
        val idDetailProvider: Int?,
        val idDetail: Int,
        val idProvider: Int,
        val price: Float
) : BaseModel {

    override fun makeInsertString(): String =
            "INSERT INTO DetailProvider values (default, $idDetail, $idProvider, $price)"

    override fun makeUpdateString(): String = "UPDATE DetailProvider SET " +
            "$COLUMN_ID_DETAIL = $idDetail," +
            "$COLUMN_ID_PROVIDER = $idProvider," +
            "$COLUMN_PRICE = $price " +
            "WHERE $COLUMN_ID_DETAIL_PROVIDER = $idDetailProvider"

    companion object {
        const val COLUMN_ID_DETAIL_PROVIDER = "idDetailProvider"
        const val COLUMN_ID_DETAIL = "idDetail"
        const val COLUMN_ID_PROVIDER = "idProvider"
        const val COLUMN_PRICE = "price"
    }
}