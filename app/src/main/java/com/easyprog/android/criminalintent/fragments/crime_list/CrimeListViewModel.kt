package com.easyprog.android.criminalintent.fragments.crime_list

import androidx.lifecycle.ViewModel
import com.easyprog.android.criminalintent.repository.CrimeRepository

class CrimeListViewModel: ViewModel() {

    private val crimeRepository = CrimeRepository.get()

    val crimeListLiveData = crimeRepository.getCrimes()

}