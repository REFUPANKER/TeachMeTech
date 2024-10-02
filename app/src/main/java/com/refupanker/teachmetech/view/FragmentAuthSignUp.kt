package com.refupanker.teachmetech.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.refupanker.teachmetech.databinding.FragmentAuthSignUpBinding
import com.refupanker.teachmetech.model.mdl_user

class FragmentAuthSignUp : Fragment() {

    private var _binding: FragmentAuthSignUpBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAuthSignUpBinding.inflate(inflater, container, false)

        binding.AuthSignUpButton.setOnClickListener { TrySignUp() }

        return binding.root
    }

    private fun TrySignUp() {
        binding.AuthSignUpButton.isEnabled = false
        // check fields
        if (binding.AuthSignUpUsername.text?.isEmpty() == true) {
            binding.AuthSignUpUsername.setError("Tell us your name , please :) ")
            binding.AuthSignUpButton.isEnabled = true
            return
        }
        if (binding.AuthSignUpEmail.text?.isEmpty() == true) {
            binding.AuthSignUpEmail.setError("dont wory we arent stealing emais")
            binding.AuthSignUpButton.isEnabled = true
            return
        }
        if (binding.AuthSignUpPassword.text?.isEmpty() == true) {
            binding.AuthSignUpPassword.setError("we want to secure you with password")
            binding.AuthSignUpButton.isEnabled = true
            return
        }

        auth.createUserWithEmailAndPassword(
            binding.AuthSignUpEmail.text.toString(),
            binding.AuthSignUpPassword.text.toString()
        ).addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                val signedUser = auth.currentUser
                if (signedUser != null) {
                    // create user node
                    val newUser = mdl_user(
                        signedUser.uid.toString(),
                        binding.AuthSignUpUsername.text.toString(),
                        0,
                    )

                    /* Structure
                        Users(collection)
                            user-token(doc)
                                user data
                            user-token(doc)
                                user data
                    */

                    db.collection("Users").document(signedUser.uid).set(newUser)
                        .addOnCompleteListener() { dbw ->
                            if (dbw.isSuccessful) {
                                // go to main page
                                startActivity(Intent(requireContext(), ActivityMain::class.java))
                                requireActivity().finish()
                            } else {
                                Toast.makeText(
                                    binding.root.context,
                                    "Cant save your data right now",
                                    Toast.LENGTH_SHORT
                                ).show()
                                binding.AuthSignUpButton.isEnabled = true
                            }
                        }
                }
            } else {
                when (task.exception) {
                    is FirebaseAuthWeakPasswordException -> {
                        binding.AuthSignUpPassword.error = "Password is too weak"
                    }

                    is FirebaseAuthInvalidCredentialsException -> {
                        binding.AuthSignUpEmail.error = "Email is invalid"
                    }

                    is FirebaseAuthUserCollisionException -> {
                        binding.AuthSignUpEmail.error = "Email is already in use"
                    }
                }
                binding.AuthSignUpButton.isEnabled = true
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}