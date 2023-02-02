package com.easyprog.android.criminalintent.database.entity

import android.text.format.DateFormat
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Crime(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    var title: String = "",
    var date: Date = DateFormat.format("EEEE, MMMM MM, yyyy", System.currentTimeMillis()) as Date,
    var isSolved: Boolean = false,
    var requiresPolice: Boolean = false
)