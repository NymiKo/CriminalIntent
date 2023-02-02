package com.easyprog.android.criminalintent

import android.app.Application
import com.easyprog.android.criminalintent.repository.CrimeRepository

class CriminalIntentApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        CrimeRepository.initialize(this)
    }

}