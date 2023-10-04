package com.jazepsivulis.concentration.common

import java.text.SimpleDateFormat
import java.util.*

fun Long.toTimeString(): String {
    val formatter = SimpleDateFormat("mm:ss:SSS", Locale.getDefault())
    return formatter.format(Date(this))
}

fun Long.toDateString(): String {
    val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault())
    return formatter.format(Date(this))
}
