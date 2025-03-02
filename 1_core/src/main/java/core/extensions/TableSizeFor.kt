package core.extensions

fun tableSizeFor(count: Int): Int {
    return (count / LOAD_FACTOR).toInt() + 1
}

private const val LOAD_FACTOR = 0.75
