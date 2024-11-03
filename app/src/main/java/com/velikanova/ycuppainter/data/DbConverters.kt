package com.velikanova.ycuppainter.data

import androidx.room.TypeConverter
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Date

object DbConverters {
    @TypeConverter
    fun longToZonedDate(time: Long?): ZonedDateTime? {
        return if (time == null) {
            null
        } else {
            ZonedDateTime.ofInstant(Date(time).toInstant(), ZoneId.systemDefault())
        }
    }

    @TypeConverter
    fun zonedDateToString(date: ZonedDateTime?): Long? {
        return date?.toInstant()?.epochSecond?.times(1000)
    }
}