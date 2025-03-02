package com.vazh2100.core.extensions

import kotlin.random.Random

/** предназначено для коллекций с уникальными значениями */
fun <T> Collection<T>.takeRandomWith(count: Int, with: T): List<T> {
    require(count > 0) { "Count must be greater than 0" }
    return if (count.toDouble() / this.size > SHUFFLE_THRESHOLD_RATIO) {
        this.takeRandomWithUsingShuffle(count, with)
    } else {
        this.takeRandomWithUsingSet(count, with)
    }
}

/** shuffled() + take() */
private fun <T> Collection<T>.takeRandomWithUsingShuffle(count: Int, with: T): List<T> {
    val list = this.shuffled().take(count)
    val contains = list.contains(with)
    return when {
        // Если список меньше и там нет элемента, вставляем рандомно
        count > list.size && !contains -> {
            list.toMutableList().apply { add(Random.nextInt(until = list.size + 1), with) }
        }
        // если список равен и в нём нет элемента, заменяем рандомно
        // count == list.size (count < list.size невозможно)
        count <= list.size && !contains -> {
            list.toMutableList().apply { this[Random.nextInt(until = list.size)] = with }
        }
        else -> list // если список меньше или равен, а элемент присутствует, не трогаем
    }
}

private fun <T> Collection<T>.takeRandomWithUsingSet(count: Int, with: T): List<T> {
    val list = if (this is List) this else this.toList()
    val resultSet = HashSet<T>(tableSizeFor(count))
    resultSet.add(with)
    while (resultSet.size < count) {
        resultSet.add(list[Random.nextInt(list.size)])
    }
    return resultSet.shuffled()
}

private const val SHUFFLE_THRESHOLD_RATIO = 0.12
