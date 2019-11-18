package com.duwna.utils

import com.duwna.models.Detail
import com.duwna.models.User
import kotlin.random.Random

fun generateUser(): User = User(
        null,
        generateString(),
        generateString(),
        (0..3).random().toShort(),
        generateString(),
        generateString(),
        (10000000000L..99999999999L).random()
)

fun generateDetail(): Detail = Detail(
        null,
        generateString(),
        Random.nextFloat(),
        Random.nextBoolean()
)

fun generateString(count: Int = 10) = buildString {
    for (i in 0..count)
        append(('A'..'Z').random())
}

