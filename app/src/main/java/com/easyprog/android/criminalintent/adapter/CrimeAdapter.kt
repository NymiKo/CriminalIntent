package com.easyprog.android.criminalintent.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.easyprog.android.criminalintent.R
import com.easyprog.android.criminalintent.database.entity.Crime

class CrimeAdapter(var crimes: List<Crime>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            0 -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_crime, parent, false)
                CrimeListViewHolders.CrimeHolder(view)
            }
            1 -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_crime_with_police, parent, false)
                CrimeListViewHolders.CrimeHolderPolice(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_crime, parent, false)
                CrimeListViewHolders.CrimeHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val crime = crimes[position]
        when(crime.requiresPolice) {
            true -> (holder as CrimeListViewHolders.CrimeHolderPolice).bind(crime)
            false -> (holder as CrimeListViewHolders.CrimeHolder).bind(crime)
        }
    }

    override fun getItemCount(): Int = crimes.size

    override fun getItemViewType(position: Int): Int {
        val crime = crimes[position]
        return when(crime.requiresPolice) {
            true -> 1
            false -> 0
        }
    }
}