package com.refupanker.teachmetech.view

import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.ActionBar.LayoutParams
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.refupanker.teachmetech.databinding.ActivityCourseBinding
import com.refupanker.teachmetech.model.mdl_course
import com.refupanker.teachmetech.model.mdl_sub_title
import kotlinx.coroutines.launch

class ActivityCourse : AppCompatActivity() {

    private var _binding: ActivityCourseBinding? = null
    private val binding get() = _binding!!

    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCourseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val course = intent.getSerializableExtra("course") as mdl_course

        binding.CourseGoBack.setOnClickListener { finish() }

        binding.CourseTitle.text = course.title
        binding.CourseHeaderLikes.text = course.likes.toString()
        binding.CourseHeaderDescription.text = course.description
        binding.CourseHeaderCategory.text = course.category
        //TODO: add steps system
        lifecycleScope.launch {
            binding.CourseStatus.visibility = View.VISIBLE
            binding.CourseStatus.text = "Loading ..."
            db.collection("CourseContents").document(course.token).get()
                .addOnCompleteListener { t ->
                    try {
                        if (t.isSuccessful) {
                            for (j in t.result.data!!.get("data") as List<Map<String, Any>>) {
                                when (j.get("type")) {
                                    "video" -> {
                                        val newVideo = YouTubePlayerView(baseContext)
                                        newVideo.layoutParams = ViewGroup.LayoutParams(
                                            ViewGroup.LayoutParams.MATCH_PARENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT
                                        )

                                        lifecycle.addObserver(newVideo)
                                        newVideo.addYouTubePlayerListener(object :
                                            AbstractYouTubePlayerListener() {
                                            override fun onReady(youTubePlayer: YouTubePlayer) {
                                                youTubePlayer.cueVideo(
                                                    j.get("target").toString(), 0f
                                                )
                                            }
                                        })

                                        binding.CourseContentHolder.addView(newVideo)
                                    }

                                    "title" -> {
                                        val mdl = mdl_sub_title(
                                            text = j.get("text").toString(),
                                            size = j.get("size") as Long
                                        )
                                        val newText = TextView(baseContext)
                                        newText.text = mdl.text
                                        newText.setTextColor(Color.WHITE)
                                        newText.textSize = mdl.size.toFloat()
                                        val layoutParams = ViewGroup.MarginLayoutParams(
                                            LayoutParams.MATCH_PARENT,
                                            LayoutParams.WRAP_CONTENT,
                                        )
                                        layoutParams.bottomMargin = (mdl.size / 2).toInt()
                                        layoutParams.topMargin = (mdl.size / 3).toInt()
                                        newText.layoutParams = layoutParams

                                        binding.CourseContentHolder.addView(newText)
                                    }

                                    "paragraph" -> {
                                        val newText = TextView(baseContext)
                                        newText.text = j.get("text").toString()
                                        newText.setTextColor(Color.parseColor("#BEBEBE"))
                                        newText.textSize = 16f
                                        newText.layoutParams =
                                            ViewGroup.LayoutParams(
                                                LayoutParams.MATCH_PARENT,
                                                LayoutParams.WRAP_CONTENT
                                            )
                                        binding.CourseContentHolder.addView(newText)
                                    }

                                    "link" -> {
                                        val newText = TextView(baseContext)
                                        newText.text = j.get("text").toString()
                                        newText.paintFlags =
                                            newText.paintFlags or Paint.UNDERLINE_TEXT_FLAG
                                        newText.setTextColor(Color.parseColor("#37A4ED"))
                                        newText.textSize = 16f
                                        newText.layoutParams =
                                            ViewGroup.LayoutParams(
                                                LayoutParams.MATCH_PARENT,
                                                LayoutParams.WRAP_CONTENT
                                            )
                                        newText.setOnClickListener {
                                            val intent = Intent(
                                                Intent.ACTION_VIEW,
                                                Uri.parse(j.get("target").toString())
                                            )
                                            ContextCompat.startActivity(
                                                baseContext, intent, null
                                            )
                                        }
                                        binding.CourseContentHolder.addView(newText)
                                    }
                                }
                            }
                            binding.CourseStatus.visibility = View.GONE
                        } else {
                            binding.CourseStatus.text = "Cant get course data"
                        }
                    } catch (e: Exception) {
                        binding.CourseStatus.text = "Cant get course data"
                    }
                }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}