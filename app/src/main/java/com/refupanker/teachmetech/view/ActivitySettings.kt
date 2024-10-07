package com.refupanker.teachmetech.view

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.refupanker.teachmetech.R
import com.refupanker.teachmetech.databinding.ActivitySettingsBinding

class ActivitySettings : AppCompatActivity() {
    private var _binding: ActivitySettingsBinding? = null
    public val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.SettingsGoBack.setOnClickListener { finish() }
        binding.ProfileLogout.setOnClickListener {
            Toast.makeText(baseContext, "Logging out", Toast.LENGTH_SHORT).show()
            Firebase.auth.signOut()
            val intent = Intent(this, ActivityAuth::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        binding.navigationView.setNavigationItemSelectedListener { i ->
            when (i.itemId) {
                R.id.item_settings_update_pfp -> ShowSubFragment(FragmentSettingsUpdatePfp())

                R.id.item_settings_update_username -> Toast.makeText(
                    this,
                    "Update username",
                    Toast.LENGTH_SHORT
                ).show()

                R.id.item_settings_update_aboutme -> Toast.makeText(
                    this,
                    "Update aboutme",
                    Toast.LENGTH_SHORT
                ).show()
            }
            false
        }
    }

    fun ShowSubFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_right
            )
            .disallowAddToBackStack()
            .replace(R.id.settings_frame, fragment)
            .commit()
    }

    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.settings_frame)
        if (currentFragment == null) {
            super.onBackPressed()
        } else {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_out_right
                )
                .remove(currentFragment)
                .commit()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}