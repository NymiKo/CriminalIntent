package com.easyprog.android.criminalintent.adapter

import androidx.recyclerview.widget.DiffUtil
import com.easyprog.android.criminalintent.database.entity.Crime

class CrimeItemDiffCallback: DiffUtil.ItemCallback<Crime>() {
    override fun areItemsTheSame(oldItem: Crime, newItem: Crime): Boolean = oldItem == newItem

    override fun areContentsTheSame(oldItem: Crime, newItem: Crime): Boolean = oldItem == newItem
}