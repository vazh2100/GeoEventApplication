package core.extensions

val String.capitalizeFirst: String
    get() = this.replaceFirstChar { it.uppercaseChar() }
