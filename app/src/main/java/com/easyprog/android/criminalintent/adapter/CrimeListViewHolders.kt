package com.easyprog.android.criminalintent.adapter

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.easyprog.android.criminalintent.R
import com.easyprog.android.criminalintent.database.entity.Crime
import com.easyprog.android.criminalintent.fragments.crime_list.Callbacks
import com.easyprog.android.criminalintent.fragments.crime_list.CrimeListFragment
import java.util.*

class CrimeListViewHolders {



    class CrimeHolderPolice(view: View, val callbacks: Callbacks?): RecyclerView.ViewHolder(view), View.OnClickListener{
        private val titleTextView: TextView = itemView.findViewById(R.id.crime_title_police)
        private val dateTextView: TextView = itemView.findViewById(R.id.crime_date_police)
        private val policeButton: ImageButton = itemView.findViewById(R.id.requires_police_button)

        private lateinit var crime: Crime

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(crime: Crime) {
            this.crime = crime
            titleTextView.text = this.crime.title
            dateTextView.text = this.crime.date.toString()
        }

        override fun onClick(v: View?) {
            callbacks?.onCrimeSelected(crime.id)
        }
    }

}