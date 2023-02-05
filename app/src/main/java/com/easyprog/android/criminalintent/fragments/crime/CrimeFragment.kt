package com.easyprog.android.criminalintent.fragments.crime

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickContact
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.ViewModelProvider
import com.easyprog.android.criminalintent.R
import com.easyprog.android.criminalintent.database.entity.Crime
import com.easyprog.android.criminalintent.fragments.date_picker.DatePickerFragment
import java.io.File
import java.util.*

class CrimeFragment : Fragment(), FragmentResultListener {

    companion object {
        private const val ARG_CRIME_ID = "crime_id"
        private const val DIALOG_DATE = "DialogDate"
        private const val REQUEST_DATE = "RequestDate"
        private const val RESULT_DATE_KEY = "ARG_RESULT_DATE"
        private const val DATE_FORMAT = "EEE, MMM, dd"

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
    private lateinit var photoFile: File
    private lateinit var photoUri: Uri

    private lateinit var titleField: EditText
    private lateinit var dateButton: Button
    private lateinit var reportButton: Button
    private lateinit var suspectButton: Button
    private lateinit var photoButton: ImageButton
    private lateinit var photoView: ImageView
    private lateinit var callSuspectButton: ImageButton
    private lateinit var solvedCheckBox: CheckBox

    private val launcher = getContactName()
    private val launcherPermission = checkPermissionForContacts { launcher.launch(null) }
    private val launcherPhotoResult = getPhotoResult()

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
        reportButton = view.findViewById(R.id.crime_report) as Button
        suspectButton = view.findViewById(R.id.crime_suspect) as Button
        photoButton = view.findViewById(R.id.crime_camera) as ImageButton
        callSuspectButton = view.findViewById(R.id.call_suspect) as ImageButton
        solvedCheckBox = view.findViewById(R.id.crime_solved) as CheckBox
        photoView = view.findViewById(R.id.crime_photo) as ImageView

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.crimeIdLiveData.observe(viewLifecycleOwner) { crime ->
            crime?.let {
                this.crime = crime
                photoFile = viewModel.getPhotoFile(crime)
                photoUri = FileProvider.getUriForFile(requireActivity(), "com.easyprog.android.criminalintent.fileprovider", photoFile)
                updateUI()
            }
        }
        childFragmentManager.setFragmentResultListener(REQUEST_DATE, viewLifecycleOwner, this)
    }

    override fun onStart() {
        super.onStart()
        val titleWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                sequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
            }

            override fun onTextChanged(
                sequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
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

        reportButton.setOnClickListener {
            Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, getCrimeReport())
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject))
            }.also { intent ->
                val chooseIntent = Intent.createChooser(intent, getString(R.string.send_report))
                startActivity(chooseIntent)
            }
        }

        suspectButton.apply {
            val pickIntent = Intent(Intent.ACTION_VIEW, ContactsContract.Contacts.CONTENT_URI)
            val packageManager = requireActivity().packageManager
            val resolvedActivity =
                packageManager.queryIntentActivities(pickIntent, PackageManager.MATCH_DEFAULT_ONLY)
            if (resolvedActivity.size != 0) {
                setOnClickListener {
                    launcherPermission.launch(android.Manifest.permission.READ_CONTACTS)
                }
            } else {
                isEnabled = false
            }
        }

        callSuspectButton.setOnClickListener {
            viewModel.contactNumber.observe(viewLifecycleOwner) {
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:$it")
                }
                startActivity(intent)
            }
        }

        photoButton.apply {
            val packageManager: PackageManager = requireActivity().packageManager
            val captureImage = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val resolvedActivity: ResolveInfo? = packageManager.resolveActivity(captureImage, PackageManager.MATCH_DEFAULT_ONLY)
            if (resolvedActivity == null) {
                isEnabled = false
            }

            setOnClickListener {
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                val cameraActivities: List<ResolveInfo> = packageManager.queryIntentActivities(captureImage, PackageManager.MATCH_DEFAULT_ONLY)
                for (cameraActivity in cameraActivities) {
                    requireActivity().grantUriPermission(cameraActivity.activityInfo.packageName, photoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                }

                launcherPhotoResult.launch(captureImage)
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
        if (crime.suspect.isNotEmpty()) {
            suspectButton.text = crime.suspect
        }
    }

    private fun getCrimeReport(): String {
        val solvedString = if (crime.isSolved) getString(R.string.crime_report_solved)
        else getString(R.string.crime_report_unsolved)

        val dateString = DateFormat.format(DATE_FORMAT, crime.date).toString()
        val suspect = if (crime.suspect.isBlank()) getString(R.string.crime_report_no_suspect)
        else getString(R.string.crime_report_suspect, crime.suspect)
        return getString(R.string.crime_report, crime.title, dateString, solvedString, suspect)
    }

    private fun dateResult(result: Bundle) = result.getSerializable(RESULT_DATE_KEY) as Date

    override fun onFragmentResult(requestKey: String, result: Bundle) {
        when (requestKey) {
            REQUEST_DATE -> {
                crime.date = dateResult(result)
                updateUI()
            }
        }
    }

    private fun checkPermissionForContacts(block: () -> Unit) =
        registerForActivityResult(RequestPermission()) { isGranted ->
            if (isGranted) {
                block()
            }
        }

    private fun getPhotoResult() = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { photoResult ->
        if (photoResult.resultCode == Activity.RESULT_OK) {

        }
    }

    private fun getContactName() = registerForActivityResult(PickContact()) { result ->
        val queryFields = arrayOf(ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts._ID)
        val cursor = result?.let {
            requireActivity().contentResolver.query(it, queryFields, null, null, null)
        }
        cursor?.use {
            if (it.count > 0) {
                it.moveToFirst()
                val suspect = it.getString(0)
                crime.suspect = suspect
                viewModel.saveCrime(crime)
                suspectButton.text = suspect

                val contactId = it.getString(1)
                val phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                val phoneNumberQueryFields = arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER)
                val phoneWhereClause = "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?"
                val phoneQueryParameters = arrayOf(contactId)
                val phoneCursor = requireActivity().contentResolver.query(
                    phoneUri,
                    phoneNumberQueryFields,
                    phoneWhereClause,
                    phoneQueryParameters,
                    null
                )
                phoneCursor?.use { cursorPhone ->
                    cursorPhone.moveToFirst()
                    viewModel.saveNumber(cursorPhone.getString(0))
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.saveCrime(crime)
    }
}