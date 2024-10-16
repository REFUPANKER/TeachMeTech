package com.refupanker.teachmetech.view

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.refupanker.teachmetech.adapter.adapter_badges
import com.refupanker.teachmetech.databinding.FragmentProfileBinding
import com.refupanker.teachmetech.model.mdl_badge
import com.refupanker.teachmetech.model.mdl_user
import com.refupanker.teachmetech.model.mdl_userprops
import kotlinx.coroutines.launch
import java.io.ByteArrayInputStream
import java.util.UUID


class FragmentProfile : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    public val binding get() = _binding!!

    private val badges: ArrayList<mdl_badge> = arrayListOf()
    private var adapter_badge: adapter_badges? = null

    private val auth = Firebase.auth
    private val storage = Firebase.storage.reference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val ctx = requireContext()

        adapter_badge = adapter_badges(badges)
        binding.ProfileBadges.adapter = adapter_badge
        binding.ProfileBadges.layoutManager = GridLayoutManager(ctx, 8)

        binding.ProfileSettings.setOnClickListener {
            val settings = Intent(requireContext(), ActivitySettings::class.java)
            startActivity(settings)
        }


        GetUserData()

        return binding.root
    }


    private fun GetUserData() {
        lifecycleScope.launch {
            //get User
            binding.ProfileUsername.text = "Loading ..."
            binding.ProfileRank.text = "Loading ..."
            Firebase.firestore.collection("Users")
                .document(auth.currentUser?.uid.toString()).get()
                .addOnCompleteListener { t ->
                    try {
                        if (t.isSuccessful) {
                            val user = mdl_user(
                                token = t.result.data?.get("token") as String,
                                name = t.result.data!!["name"] as String,
                                aboutMe = t.result.data!!["aboutMe"] as String,
                                rank = t.result.data!!["rank"] as Long,
                                active = t.result.data!!["active"] as Boolean
                            )
                            //TODO: rank scale
                            binding.ProfileRankProgress.progress = user.rank.toInt()
                            binding.ProfileRankProgress.max = user.rank.toInt() * 2
                            binding.ProfileRank.text =
                                user.rank.toString() + "/" + binding.ProfileRankProgress.max
                            binding.ProfileUsername.text = user.name
                            binding.ProfileAboutme.text = user.aboutMe

                            // Get user profile photo
                            binding.ProfilePfpLoading.visibility = View.VISIBLE
                            storage.child("ProfilePhotos/${user.token}")
                                .getBytes(Long.MAX_VALUE)
                                .addOnCompleteListener { t ->
                                    try {
                                        if (t.isSuccessful) {
                                            val bmp = BitmapFactory.decodeByteArray(t.result, 0, t.result.size)

                                            val exifInterface =
                                                androidx.exifinterface.media.ExifInterface(
                                                    ByteArrayInputStream(t.result)
                                                )
                                            val orientation = exifInterface.getAttributeInt(
                                                androidx.exifinterface.media.ExifInterface.TAG_ORIENTATION,
                                                androidx.exifinterface.media.ExifInterface.ORIENTATION_NORMAL
                                            )

                                            val matrix = Matrix()
                                            when (orientation) {
                                                androidx.exifinterface.media.ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
                                                androidx.exifinterface.media.ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
                                                androidx.exifinterface.media.ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
                                            }

                                            val rotatedBmp = Bitmap.createBitmap(
                                                bmp, 0, 0, bmp.width, bmp.height, matrix, true
                                            )

                                            binding.ProfilePfp.setImageBitmap(rotatedBmp)
                                        } else {
                                            binding.ProfilePfp.imageTintList =
                                                ColorStateList.valueOf(
                                                    Color.WHITE
                                                )
                                        }
                                        binding.ProfilePfpLoading.visibility = View.GONE
                                    } catch (e: Exception) {
                                    }
                                }

                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Cant get user data",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: Exception) {

                    }
                }
            //get UserProps
            binding.ProfileAboutme.text = "Loading ..."
            binding.textView7.text = "Badges Loading ..."
            Firebase.firestore.collection("UserProps")
                .document(auth.currentUser?.uid.toString()).get()
                .addOnCompleteListener { t ->
                    try {
                        if (t.isSuccessful) {
                            val userprops = mdl_userprops(
                                token = t.result.data?.get("token") as String,
                                likedCourses = t.result.data!!["likedCourses"] as List<String>,
                                badges = t.result.data!!["badges"] as List<String>,
                                shownBadge = t.result.data!!["shownBadge"] as String
                            )
                            if (userprops.badges.isEmpty()) {
                                binding.textView7.text = "Badges (No badges)"
                            }
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Cant get user data",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: Exception) {

                    }
                }
        }

    }

    fun GetBadges() {
        badges.clear()
        for (i in 0..4) {
            val token = UUID.randomUUID().toString()
            badges.add(
                mdl_badge(
                    token,
                    "My Proud Title",
                )
            )
        }
        badges.sortBy { m -> m.name }
        adapter_badge?.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}