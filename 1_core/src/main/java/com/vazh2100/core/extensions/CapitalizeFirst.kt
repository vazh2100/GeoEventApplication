package com.vazh2100.core.extensions

val String.capitalizeFirst: String
    get() = this.replaceFirstChar { it.uppercaseChar() }
