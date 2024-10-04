package com.refupanker.teachmetech.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.refupanker.teachmetech.R
import com.refupanker.teachmetech.databinding.ActivityAuthBinding

class ActivityAuth : AppCompatActivity() {
    private var _binding: ActivityAuthBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAuthBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val auth = Firebase.auth
        if (auth.currentUser != null) {
            startActivity(Intent(this, ActivityMain::class.java))
            finish()
            return
        }

        setContentFragment(FragmentAuthSignUp())

        binding.authBottombar.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item_auth_signup -> setContentFragment(FragmentAuthSignUp())
                R.id.item_auth_login -> setContentFragment(FragmentAuthLogin())
            }
            true
        }


    }

    fun setContentFragment(fragment: Fragment) {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.auth_frame)
        if (currentFragment != null && currentFragment::class == fragment::class) {
            Toast.makeText(this, "Already shown", Toast.LENGTH_SHORT).show()
        } else {
            supportFragmentManager.beginTransaction()
                .disallowAddToBackStack()
                .replace(R.id.auth_frame, fragment)
                .commit()
        }
    }

    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.auth_frame)
        if (currentFragment is FragmentAuthSignUp) {
            super.onBackPressed()
        } else {
            binding.authBottombar.selectedItemId = R.id.item_auth_signup
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}