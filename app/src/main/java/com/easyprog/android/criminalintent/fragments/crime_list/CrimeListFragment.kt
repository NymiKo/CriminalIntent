package com.easyprog.android.criminalintent.fragments.crime_list

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.easyprog.android.criminalintent.R
import com.easyprog.android.criminalintent.adapter.CrimeAdapter
import com.easyprog.android.criminalintent.database.entity.Crime

class CrimeListFragment : Fragment() {

    private var callbacks: Callbacks? = null

    companion object {
        fun newInstance(): CrimeListFragment {
            return CrimeListFragment()
        }
    }

    private val viewModel: CrimeListViewModel by lazy { ViewModelProvider(this)[CrimeListViewModel::class.java] }

    private lateinit var crimeRecyclerView: RecyclerView
    private var adapter: CrimeAdapter? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks
        adapter = CrimeAdapter(callbacks)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)

        crimeRecyclerView = view.findViewById(R.id.crime_recycler_view) as RecyclerView
        crimeRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        crimeRecyclerView.adapter = adapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.crimeListLiveData.observe(viewLifecycleOwner) { crimes ->
            crimes?.let { updateUI(crimes) }
        }
    }

    private fun updateUI(crimes: List<Crime>) {
        adapter?.submitList(crimes)
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }
}