package com.bluedragoon.barcodescannerlite.utils

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimeDeserializer: JsonDeserializer<LocalDateTime> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): LocalDateTime? {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(ISO_DATE_TIME_PATTERN)
//        return ZonedDateTime.parse(json?.asJsonPrimitive?.asString, formatter).toLocalDateTime()
        return LocalDateTime.parse(json?.asJsonPrimitive?.asString, formatter)
//        return ZonedDateTime.parse(json?.asJsonPrimitive.toString()).toLocalDateTime()
//        return try {
//            ZonedDateTime.parse(json?.asJsonPrimitive.toString()).toLocalDateTime()
//        }catch (e: JsonParseException){
//            System.err.println("Failed to parse Date: $e")
//            null
//        }
    }

}