package com.easyprog.android.criminalintent.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.easyprog.android.criminalintent.R
import com.easyprog.android.criminalintent.adapter.CrimeAdapter.*
import com.easyprog.android.criminalintent.database.entity.Crime
import com.easyprog.android.criminalintent.fragments.crime_list.Callbacks

class CrimeAdapter(var crimes: List<Crime>, val callbacks: Callbacks?): ListAdapter<Crime, CrimeHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_crime, parent, false)
        return CrimeHolder(view)
    }

    override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
        val crime = crimes[position]
        holder.bind(crime)
    }

    override fun getItemCount(): Int = crimes.size

    inner class CrimeHolder(view: View): RecyclerView.ViewHolder(view), View.OnClickListener{
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
            callbacks?.onCrimeSelected(crime.id)
        }
    }
}