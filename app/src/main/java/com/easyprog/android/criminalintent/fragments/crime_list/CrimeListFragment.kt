package com.easyprog.android.criminalintent.fragments.crime_list

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
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

    private lateinit var emptyListTextView: TextView
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

        emptyListTextView = view.findViewById(R.id.empty_list_text_view) as TextView
        crimeRecyclerView = view.findViewById(R.id.crime_recycler_view) as RecyclerView
        crimeRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        crimeRecyclerView.adapter = adapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMenu()
        viewModel.crimeListLiveData.observe(viewLifecycleOwner) { crimes ->
            if (crimes.isNotEmpty()) {
                crimes?.let { updateUI(crimes) }
                crimeRecyclerView.visibility = View.VISIBLE
                emptyListTextView.visibility = View.GONE
            } else {
                emptyListTextView.visibility = View.VISIBLE
            }
        }
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.fragment_crime_list, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when(menuItem.itemId) {
                    R.id.new_crime -> {
                        val crime = Crime()
                        viewModel.addCrime(crime)
                        callbacks?.onCrimeSelected(crime.id)
                        true
                    }
                    else -> true
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun updateUI(crimes: List<Crime>) {
        adapter?.submitList(crimes)
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }
}