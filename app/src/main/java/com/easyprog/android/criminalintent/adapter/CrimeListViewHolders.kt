package com.easyprog.android.criminalintent.adapter

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.easyprog.android.criminalintent.R
import com.easyprog.android.criminalintent.database.entity.Crime

class CrimeListViewHolders {

    class CrimeHolder(view: View): RecyclerView.ViewHolder(view), View.OnClickListener{
        private val titleTextView: TextView = itemView.findViewById(R.id.crime_title)
        private val dateTextView: TextView = itemView.findViewById(R.id.crime_date)
        private val solvedImageView: ImageView = itemView.findViewById(R.id.crime_solved)

        private lateinit var crime: Crime

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(crime: Crime) {
            this.crime = crime
            titleTextView.text = this.crime.title
            dateTextView.text = this.crime.date.toString()
            solvedImageView.visibility = if (crime.isSolved) View.VISIBLE else View.GONE
        }

        override fun onClick(v: View?) {

        }
    }

    class CrimeHolderPolice(view: View): RecyclerView.ViewHolder(view), View.OnClickListener{
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

        }
    }

}