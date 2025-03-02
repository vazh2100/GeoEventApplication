package core.extensions

fun <T> Boolean.ifElse(ify: T, elsy: T): T = if (this) ify else elsy
