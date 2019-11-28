package com.duwna.models

data class Order (
        val idOrder: Int?,
        val orderDateTime: String,
        val expectedDayTime: String,
        val deliveryDateTime: String?
) : BaseModel {

    override fun makeInsertString(): String = "INSERT INTO `Order` VALUES (default, '$orderDateTime', '$expectedDayTime', null)"

    override fun makeUpdateString(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    companion object {
        const val ID_ORDER = "idOrder"
        const val ORDER_DATETIME = "orderDateTime"
        const val EXPECTED_DAYTIME = "expectedDayTime"
        const val DELIVERY_DATETIME = "deliveryDateTime"
    }
}