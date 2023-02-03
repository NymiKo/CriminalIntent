package com.easyprog.android.criminalintent.fragments.crime_list

import androidx.lifecycle.ViewModel
import com.easyprog.android.criminalintent.database.entity.Crime
import com.easyprog.android.criminalintent.repository.CrimeRepository

class CrimeListViewModel: ViewModel() {

    private val crimeRepository = CrimeRepository.get()

    val crimeListLiveData = crimeRepository.getCrimes()

    fun addCrime(crime: Crime) {
        crimeRepository.addCrime(crime)
    }
}