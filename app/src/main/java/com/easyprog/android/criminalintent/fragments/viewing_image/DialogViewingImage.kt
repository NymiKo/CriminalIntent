package com.easyprog.android.criminalintent.fragments.viewing_image

import android.app.Dialog
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.easyprog.android.criminalintent.R

class DialogViewingImage: DialogFragment() {

    companion object {
        private const val PHOTO_NAME = "PHOTO_NAME"

        fun newInstance(imageName: String): DialogViewingImage {
            val args = Bundle().apply {
                putString(PHOTO_NAME, imageName)
            }
            return DialogViewingImage().apply {
                arguments = args
            }
        }
    }

    private lateinit var imageZoom: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dialog_viewing_image, container, false)
        imageZoom = view.findViewById(R.id.image_view_zoom) as ImageView

        val imageName = requireArguments().getString(PHOTO_NAME)
        imageZoom.setImageBitmap(BitmapFactory.decodeFile(requireContext().filesDir.path + "/" + imageName))

        return view
    }
}