package com.easyprog.android.criminalintent.database.conversters

import androidx.room.TypeConverter
import java.util.Date

class CrimeTypeDateConverters {

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun toDate(millisSinceEpoch: Long?): Date? {
        return millisSinceEpoch?.let {
            Date(it)
        }
    }
}