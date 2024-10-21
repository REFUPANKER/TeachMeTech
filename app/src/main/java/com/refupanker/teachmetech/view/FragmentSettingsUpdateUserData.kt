package com.refupanker.teachmetech.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.refupanker.teachmetech.R
import com.refupanker.teachmetech.databinding.FragmentSettingsUpdateUserdataBinding
import com.refupanker.teachmetech.model.mdl_user
import kotlinx.coroutines.launch


class FragmentSettingsUpdateUserData : Fragment() {
    private var _binding: FragmentSettingsUpdateUserdataBinding? = null
    private val binding get() = _binding!!

    private val db = Firebase.firestore

    var userData: mdl_user? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsUpdateUserdataBinding.inflate(inflater, container, false)
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

        // get current user data
        binding.settingsUpdateButton.isEnabled = false
        binding.SettingsUpdateUsernameInput.hint = "Loading ..."
        binding.SettingsUpdateAboutmeInput.hint = "Loading ..."
        lifecycleScope.launch {
            db.collection("Users")
                .document(
                    Firebase.auth.currentUser!!
                        .uid.toString()
                )
                .get().addOnCompleteListener { t ->
                    try {
                        if (t.isSuccessful) {
                            userData = mdl_user(
                                token = t.result.getString("token")!!,
                                name = t.result.getString("name")!!,
                                aboutMe = t.result.getString("aboutMe")!!,
                                rank = t.result.getLong("rank")!!,
                                active = t.result.getBoolean("active")!!,
                            )
                            binding.SettingsUpdateUsernameInput.hint = userData!!.name
                            binding.SettingsUpdateAboutmeInput.hint = userData!!.aboutMe
                            binding.settingsUpdateButton.isEnabled = true
                        } else {
                            Toast.makeText(context, "an Error occured", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {

                    }
                }
        }

        binding.settingsUpdateButton.setOnClickListener { UpdateData() }

        return binding.root
    }

    fun UpdateData() {

        var updateParts: MutableMap<String?, Any?> = mutableMapOf()
        val pure_name = binding.SettingsUpdateUsernameInput.text.toString().trim()
            .replace("\n", " ")
            .replace("\u200E", "")
        val pure_aboutMe = binding.SettingsUpdateAboutmeInput.text.toString().trim()
            .replace("\n", " ")
            .replace("\u200E", "")
        if (!pure_name.isNullOrEmpty()) {
            if (pure_name.length < 3) {
                Toast.makeText(context, "Name is too short (min 4 letters)", Toast.LENGTH_SHORT)
                    .show()
                return
            }
            updateParts.put("name", binding.SettingsUpdateUsernameInput.text.toString())
        }
        if (!pure_aboutMe.isNullOrEmpty()) {
            if (pure_aboutMe.length < 10) {
                Toast.makeText(
                    context,
                    "About Me is too short (min 10 letters)",
                    Toast.LENGTH_SHORT
                )
                    .show()
                return
            }
            updateParts.put("aboutMe", pure_aboutMe)
        }

        if (updateParts.isEmpty()) {
            Toast.makeText(context, "No data updated", Toast.LENGTH_SHORT).show()
            binding.SettingsSubGoBack.callOnClick()
            return
        }

        lifecycleScope.launch {
            db.collection("Users")
                .document(Firebase.auth.currentUser!!.uid.toString())
                .update(updateParts).addOnCompleteListener { t ->
                    if (t.isSuccessful) {
                        Toast.makeText(context, "User data updated", Toast.LENGTH_SHORT).show()
                        binding.SettingsSubGoBack.isEnabled = false
                        binding.SettingsSubGoBack.callOnClick()
                    } else {
                        Toast.makeText(context, "an Error occured", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}