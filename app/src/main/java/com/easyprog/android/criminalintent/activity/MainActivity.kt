package com.easyprog.android.criminalintent.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.easyprog.android.criminalintent.R
import com.easyprog.android.criminalintent.fragments.crime.CrimeFragment
import com.easyprog.android.criminalintent.fragments.crime_list.Callbacks
import com.easyprog.android.criminalintent.fragments.crime_list.CrimeListFragment
import java.util.*

class MainActivity : AppCompatActivity(), Callbacks {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (currentFragment == null) {
            val fragment = CrimeListFragment.newInstance()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }

    }

    override fun onCrimeSelected(crimeId: UUID) {
        val fragment = CrimeFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}