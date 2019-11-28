package com.duwna.models

data class ContentOrder(
        val idContentOrder: Int?,
        val idOrder: Int,
        val idDetailProvider: Int,
        val quantity: Int,
        val sum: Float
) : BaseModel {

    override fun makeInsertString(): String = "INSERT INTO ContentOrder VALUES (default, $idOrder, $idDetailProvider, $quantity, $sum)"

    override fun makeUpdateString(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        const val ID_CONTENT_ORDER = "idContentOrder"
        const val ID_ORDER = "idOrder"
        const val ID_DETAIL_PROVIDER = "idDetailProvider"
        const val QUANTITY = "quantity"
        const val SUM = "sum"
    }
}
