package com.duwna.models

data class Content(
        val detail: Detail,
        val detailProvider: DetailProvider,
        var quantity: Int,
        val sum: Float
) {

    fun toContentOrder(idOrder: Int) = ContentOrder(
            null,
            idOrder,
            detailProvider.idDetailProvider!!,
            quantity,
            sum
    )
}
