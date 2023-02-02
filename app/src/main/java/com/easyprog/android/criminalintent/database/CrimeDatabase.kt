package com.easyprog.android.criminalintent.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.easyprog.android.criminalintent.database.conversters.CrimeTypeDateConverters
import com.easyprog.android.criminalintent.database.conversters.CrimeTypeUUIDConverters
import com.easyprog.android.criminalintent.database.dao.CrimeDao
import com.easyprog.android.criminalintent.database.entity.Crime

@Database(entities = [Crime::class], version = 1)
@TypeConverters(CrimeTypeDateConverters::class, CrimeTypeUUIDConverters::class)
abstract class CrimeDatabase: RoomDatabase() {

    abstract fun crimeDao(): CrimeDao

}