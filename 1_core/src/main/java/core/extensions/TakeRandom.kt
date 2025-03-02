package core.extensions

fun <T> Collection<T>.takeRandom(count: Int): List<T> {
    require(count > 0) { "Count must be greater than 0" }
    return if (count.toDouble() / this.size > SHUFFLE_THRESHOLD_RATIO) {
        this.takeRandomUsingShuffle(count)
    } else {
        this.takeRandomUsingSet(count)
    }
}

/** shuffled() + take() */
private fun <T> Collection<T>.takeRandomUsingShuffle(count: Int): List<T> {
    if (this.isEmpty()) return emptyList()
    return if (count >= this.size) this.shuffled() else this.shuffled().take(count)
}

/** Random + LinkedHashSet */
private fun <T> Collection<T>.takeRandomUsingSet(count: Int): List<T> {
    val list = if (this !is List) this.toList() else this // на случай Set
    val resultSet = LinkedHashSet<T>(tableSizeFor(count))
    while (resultSet.size < count) {
        resultSet.add(list.random())
    }
    return resultSet.toList()
}

private const val SHUFFLE_THRESHOLD_RATIO = 0.13
