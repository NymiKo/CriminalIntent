package com.easyprog.android.criminalintent.fragments.crime

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.ViewModelProvider
import com.easyprog.android.criminalintent.database.entity.Crime
import com.easyprog.android.criminalintent.R
import com.easyprog.android.criminalintent.fragments.date_picker.DatePickerFragment
import java.util.*

class CrimeFragment: Fragment(), FragmentResultListener {

    companion object {
        private const val ARG_CRIME_ID = "crime_id"
        private const val DIALOG_DATE = "DialogDate"
        private const val REQUEST_DATE = "RequestDate"
        private const val RESULT_DATE_KEY = "ARG_RESULT_DATE"

        fun newInstance(crimeId: UUID): CrimeFragment {
            val args = Bundle().apply {
                putSerializable(ARG_CRIME_ID, crimeId)
            }
            return CrimeFragment().apply {
                arguments = args
            }
        }
    }

    private val viewModel: CrimeDetailViewModel by lazy { ViewModelProvider(this)[CrimeDetailViewModel::class.java] }

    private lateinit var crime: Crime
    private lateinit var titleField: EditText
    private lateinit var dateButton: Button
    private lateinit var solvedCheckBox: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val crimeId = requireArguments().getSerializable(ARG_CRIME_ID) as UUID
        viewModel.loadCrime(crimeId)
        crime = Crime()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crime, container, false)
        titleField = view.findViewById(R.id.crime_title) as EditText
        dateButton = view.findViewById(R.id.crime_date) as Button
        solvedCheckBox = view.findViewById(R.id.crime_solved) as CheckBox

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.crimeIdLiveData.observe(viewLifecycleOwner) { crime ->
            crime?.let {
                this.crime = crime
                updateUI()
            }
        }
        childFragmentManager.setFragmentResultListener(REQUEST_DATE, viewLifecycleOwner, this)
    }

    override fun onStart() {
        super.onStart()
        val titleWatcher = object : TextWatcher {
            override fun beforeTextChanged(sequence: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun onTextChanged(sequence: CharSequence?, start: Int, before: Int, count: Int) {
                crime.title = sequence.toString()
            }

            override fun afterTextChanged(sequence: Editable?) {
            }
        }

        titleField.addTextChangedListener(titleWatcher)

        solvedCheckBox.setOnCheckedChangeListener { _, isChecked ->
            crime.isSolved = isChecked
        }

        dateButton.setOnClickListener {
            DatePickerFragment.newInstance(crime.date, REQUEST_DATE).apply {
                show(this@CrimeFragment.childFragmentManager, DIALOG_DATE)
            }
        }
    }

    private fun updateUI() {
        titleField.setText(crime.title)
        dateButton.text = crime.date.toString()
        solvedCheckBox.apply {
            isChecked = crime.isSolved
            jumpDrawablesToCurrentState()
        }
    }

    private fun dateResult(result: Bundle) = result.getSerializable(RESULT_DATE_KEY) as Date

    override fun onFragmentResult(requestKey: String, result: Bundle) {
        when(requestKey) {
            REQUEST_DATE -> {
                crime.date = dateResult(result)
                updateUI()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.saveCrime(crime)
    }
}