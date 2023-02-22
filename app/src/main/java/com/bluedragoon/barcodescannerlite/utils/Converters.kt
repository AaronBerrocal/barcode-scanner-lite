package com.bluedragoon.barcodescannerlite.utils

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

class Converters {

    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDateTime?{
//        return value?.let { LocalDateTime.ofEpochSecond(it, 0, OffsetDateTime.now().offset) }
        return value?.let {
            LocalDateTime.ofInstant(Instant.ofEpochMilli(value), ZoneOffset.UTC)
        }
    }

    @TypeConverter
    fun dateToTimestamp(ldt: LocalDateTime?): Long?{
//        return ldt?.toEpochSecond(OffsetDateTime.now().offset)
        return ldt?.atZone(ZoneOffset.UTC)?.toInstant()?.toEpochMilli()
    }

}