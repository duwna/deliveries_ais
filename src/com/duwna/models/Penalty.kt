package com.duwna.models

data class Penalty(
        val idPenalty: Int?,
        val idOrder: Int,
        val sum: Float
) : BaseModel {

    override fun makeInsertString(): String =
            "INSERT INTO Penalty VALUES (default, $idOrder, $sum)"


    override fun makeUpdateString(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        const val ID_PENALTY = "idPenalty"
        const val ID_ORDER = "idOrder"
        const val SUM = "sum"
    }
}