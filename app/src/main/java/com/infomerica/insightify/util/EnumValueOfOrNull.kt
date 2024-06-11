package com.infomerica.insightify.util

import com.google.errorprone.annotations.Keep
import org.checkerframework.checker.units.qual.K

@Keep
inline fun <reified T : Enum<T>> enumValueOfOrNull(name: String): T? =
    enumValues<T>().find { it.name == name }