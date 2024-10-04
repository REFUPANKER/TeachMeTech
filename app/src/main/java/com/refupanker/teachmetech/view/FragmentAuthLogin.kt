package com.refupanker.teachmetech.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.refupanker.teachmetech.databinding.FragmentAuthLoginBinding

class FragmentAuthLogin : Fragment() {
    private var _binding: FragmentAuthLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAuthLoginBinding.inflate(inflater, container, false)

        binding.AuthLoginButton.setOnClickListener { TryToLogin() }
        return binding.root
    }

    private fun TryToLogin() {
        binding.AuthLoginButton.isEnabled = false
        // check fields
        if (binding.AuthLoginEmail.text?.isEmpty() == true) {
            binding.AuthLoginEmail.setError("dont wory we arent stealing emails")
            binding.AuthLoginButton.isEnabled = true
            return
        }
        if (binding.AuthLoginPassword.text?.isEmpty() == true) {
            binding.AuthLoginPassword.setError("we want to secure you with password")
            binding.AuthLoginButton.isEnabled = true
            return
        }

        auth.signInWithEmailAndPassword(
            binding.AuthLoginEmail.text.toString(),
            binding.AuthLoginPassword.text.toString()
        ).addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                startActivity(Intent(requireContext(), ActivityMain::class.java))
                requireActivity().finish()
            } else {
                when (task.exception) {
                    is FirebaseAuthInvalidUserException -> {
                        binding.AuthLoginEmail.error = "User not found"
                    }

                    is FirebaseAuthInvalidCredentialsException -> {
                        binding.AuthLoginPassword.error = "Invalid password"
                    }
                }
                binding.AuthLoginButton.isEnabled = true
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}