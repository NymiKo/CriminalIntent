package com.easyprog.android.criminalintent.database.conversters

import androidx.room.TypeConverter
import java.util.UUID

class CrimeTypeUUIDConverters {

    @TypeConverter
    fun fromUUID(uuid: UUID?): String? {
        return uuid?.toString()
    }

    @TypeConverter
    fun toUUID(uuid: String?): UUID? {
        return UUID.fromString(uuid)
    }

}