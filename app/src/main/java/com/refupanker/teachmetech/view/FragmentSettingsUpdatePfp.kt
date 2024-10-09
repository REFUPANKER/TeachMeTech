package com.refupanker.teachmetech.view

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.refupanker.teachmetech.R
import com.refupanker.teachmetech.databinding.FragmentSettingsUpdatePfpBinding
import kotlinx.coroutines.launch


class FragmentSettingsUpdatePfp : Fragment() {
    private var _binding: FragmentSettingsUpdatePfpBinding? = null
    private val binding get() = _binding!!

    private val storage = Firebase.storage.reference
    private var targetUri: Uri? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsUpdatePfpBinding.inflate(inflater, container, false)
        val ctx = requireContext()

        binding.SettingsSubGoBack.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_out_right
                )
                .remove(this)
                .commit()
        }

        binding.SettingsUpdatePfpStatus.text = null

        val galleryLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                if (uri != null) {
                    try {
                        val fileSizeInBytes =
                            requireActivity().contentResolver.openFileDescriptor(uri, "r")?.use {
                                it.statSize ?: 0
                            } ?: 0

                        if (fileSizeInBytes / (1024 * 1024) <= 3) {
                            binding.SettingsUpdatePfpImage.setImageURI(uri)
                            targetUri = uri
                        } else {
                            Toast.makeText(context, "Max file size is 3MB", Toast.LENGTH_SHORT)
                                .show()
                        }
                    } catch (e: Exception) {

                    }
                }
            }

        binding.settingsUpdatePfpSelectImage.setOnClickListener { galleryLauncher.launch("image/*") }

        binding.settingsUpdateButton.setOnClickListener {
            lifecycleScope.launch {
                binding.SettingsUpdatePfpStatus.text = "Converting image ..."

                val imageBytes = context?.contentResolver?.openInputStream(targetUri!!)
                val byteRes = imageBytes?.readBytes() ?: byteArrayOf()
                imageBytes!!.close()

                binding.SettingsUpdatePfpStatus.text = "Upload starting ..."
                storage
                    .child("ProfilePhotos")
                    .child(Firebase.auth.currentUser!!.uid.toString())
                    .putBytes(byteRes)
                    .addOnProgressListener { p ->
                        try {
                            val progress = (100 * p.bytesTransferred / p.totalByteCount).toInt()
                            binding.SettingsUpdatePfpStatus.text = "Uploading ${progress}%"
                        } catch (e: Exception) {

                        }

                    }
                    .addOnCompleteListener { t ->
                        try {
                            if (t.isSuccessful) {
                                Toast.makeText(context, "Photo updated", Toast.LENGTH_SHORT).show()
                                binding.SettingsUpdatePfpStatus.text = "Upload completed"
                                binding.SettingsSubGoBack.callOnClick()
                            } else {
                                Toast.makeText(context, "an Error occured", Toast.LENGTH_SHORT)
                                    .show()
                                binding.SettingsUpdatePfpStatus.text = "Cant upload photo"
                            }
                        } catch (e: Exception) {
                        }

                    }
            }
        }


        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}