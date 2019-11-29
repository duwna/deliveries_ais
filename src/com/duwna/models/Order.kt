package com.duwna.models

data class Order (
        val idOrder: Int?,
        val orderDateTime: String,
        val expectedDate: String,
        val deliveryDate: String?
) : BaseModel {

    override fun makeInsertString(): String = "INSERT INTO `Order` VALUES (default, '$orderDateTime', '$expectedDate', null)"

    override fun makeUpdateString(): String = ""

    companion object {
        const val ID_ORDER = "idOrder"
        const val ORDER_DATETIME = "orderDateTime"
        const val EXPECTED_DATE = "expectedDate"
        const val DELIVERY_DATE = "deliveryDate"
    }
}