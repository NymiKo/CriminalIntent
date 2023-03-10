package com.easyprog.android.criminalintent.fragments.crime

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.easyprog.android.criminalintent.database.entity.Crime
import com.easyprog.android.criminalintent.repository.CrimeRepository
import java.io.File
import java.util.UUID

class CrimeDetailViewModel: ViewModel() {

    private val crimeRepository = CrimeRepository.get()

    private val _contactNumber = MutableLiveData<String>()
    val contactNumber: LiveData<String> = _contactNumber

    private val _crimeIdLiveData = MutableLiveData<UUID>()
    val crimeIdLiveData: LiveData<Crime?> = Transformations.switchMap(_crimeIdLiveData) { crimeId ->
        crimeRepository.getCrime(crimeId)
    }

    fun loadCrime(crimeId: UUID) {
        _crimeIdLiveData.value = crimeId
    }

    fun saveCrime(crime: Crime) {
        crimeRepository.updateCrime(crime)
    }

    fun saveNumber(number: String) {
        _contactNumber.value = number
    }

    fun getPhotoFile(crime: Crime): File {
        return crimeRepository.getPhotoFile(crime)
    }
}