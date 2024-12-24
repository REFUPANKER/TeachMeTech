package com.refupanker.teachmetech.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.refupanker.teachmetech.R
import com.refupanker.teachmetech.databinding.ActivityMainBinding
import com.refupanker.teachmetech.model.mdl_baninfo
import kotlinx.coroutines.launch

class ActivityMain : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (auth.currentUser == null) {
            Toast.makeText(this, "Auth required", Toast.LENGTH_SHORT).show()
            GoToAuth()
        }
        CheckBanState()

        setContentFragment(FragmentHome())

        binding.MainBottomBar.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item_main_home -> setContentFragment(FragmentHome())
                R.id.item_main_courses -> setContentFragment(FragmentCourses())
                R.id.item_main_explore -> setContentFragment(FragmentExplore())
                R.id.item_main_messages -> setContentFragment(FragmentMessages())
                R.id.item_main_profile -> setContentFragment(FragmentProfile())
            }
            true
        }
    }

    fun GoToAuth() {
        startActivity(Intent(this, ActivityAuth::class.java))
        finish()
        return
    }

    fun CheckBanState() {
        lifecycleScope.launch {
            Firebase.firestore.collection("Bans")
                .document(auth.currentUser?.uid.toString()).get()
                .addOnCompleteListener { t ->
                    try {
                        if (t.isSuccessful) {
                            val banned = Intent(baseContext, ActivityUserBanned::class.java)
                            banned.putExtra(
                                "baninfo", mdl_baninfo(
                                    t.result.id,
                                    t.result.data!!["reason"].toString(),
                                    t.result.data!!["until"].toString(),
                                )
                            )
                            auth.signOut()
                            startActivity(banned)
                            finish()
                        }
                    } catch (e: Exception) {
                    }
                }
        }
    }

    val FragmentOrder = arrayOf("home", "courses", "explore", "messages", "subdminvites", "profile")
    fun setContentFragment(fragment: Fragment) {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.main_frame)
        val targetFragmentName =
            fragment.javaClass.simpleName.lowercase().replace("fragment", "")
        if (currentFragment != null && currentFragment::class == fragment::class) {
            Toast.makeText(this, "Already shown", Toast.LENGTH_SHORT).show()
        } else {
            var animSlideIn = R.anim.slide_in_right
            var animSlideOut = R.anim.slide_out_left
            if (currentFragment != null) {
                val currentFragmentName =
                    currentFragment.javaClass.simpleName.lowercase().replace("fragment", "")
                if (FragmentOrder.indexOf(currentFragmentName) >
                    FragmentOrder.indexOf(targetFragmentName)
                ) {
                    animSlideIn = R.anim.slide_in_left
                    animSlideOut = R.anim.slide_out_right
                }
            }

            supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    animSlideIn,
                    animSlideOut,
                )
                .disallowAddToBackStack()
                .replace(R.id.main_frame, fragment)
                .commit()
        }
    }

    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.main_frame)
        if (currentFragment is FragmentHome) {
            super.onBackPressed()
        } else {
            binding.MainBottomBar.selectedItemId = R.id.item_main_home
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}