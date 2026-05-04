package com.jorgealejojara.cuentasclaras.core.data.local

import androidx.room.TypeConverter
import java.time.LocalDate

class Converters {

    @TypeConverter
    fun fromLocalDate(date: LocalDate): Long {
        return date.toEpochDay()
    }

    @TypeConverter
    fun toLocalDate(epochDay: Long): LocalDate {
        return LocalDate.ofEpochDay(epochDay)
    }
}

