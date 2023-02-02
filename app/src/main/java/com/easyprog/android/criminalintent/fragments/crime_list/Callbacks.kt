package com.easyprog.android.criminalintent.fragments.crime_list

import java.util.*

interface Callbacks {
    fun onCrimeSelected(crimeId: UUID)
}