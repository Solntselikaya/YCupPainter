package com.velikanova.ycuppainter.utils

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun ZonedDateTime.convertToReadable(): String {
    return this.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
}

fun ZonedDateTime.convertToReadableDateWithTime(): String {
    return this.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"))
}