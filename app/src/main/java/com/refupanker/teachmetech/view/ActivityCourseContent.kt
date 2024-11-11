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
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBar.LayoutParams
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.refupanker.teachmetech.R
import com.refupanker.teachmetech.databinding.ActivityCourseBinding
import com.refupanker.teachmetech.databinding.ActivityCourseContentBinding
import com.refupanker.teachmetech.model.mdl_course_content
import com.refupanker.teachmetech.model.mdl_sub_title
import kotlinx.coroutines.launch

class ActivityCourseContent : AppCompatActivity() {

    private var _binding: ActivityCourseContentBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCourseContentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val content = intent.getSerializableExtra("content") as mdl_course_content

        binding.ContentGoBack.setOnClickListener { finish() }

        binding.ContentTitle.text = content.title
        lifecycleScope.launch {
            GenerateUiObject(content)
        }
    }

    fun GenerateUiObject(content: mdl_course_content) {
        binding.ContentStatus.visibility = View.VISIBLE
        binding.ContentStatus.text = "Loading ..."
        for (j in content.data as ArrayList<Map<String, Any>>) {
            when (j.getValue("type")) {
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
                                j.getValue("target").toString(), 0f
                            )
                        }
                    })

                    binding.CourseContentHolder.addView(newVideo)
                }

                "title" -> {
                    val mdl = mdl_sub_title(
                        text = j.getValue("text").toString(),
                        size = j.getValue("size") as Long
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
                    newText.text = j.getValue("text").toString()
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
                    newText.text = j.getValue("text").toString()
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
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(j.getValue("target").toString()))
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK // Add this line
                        startActivity(intent)
                    }
                    binding.CourseContentHolder.addView(newText)
                }
            }
        }
        binding.ContentStatus.visibility = View.GONE

    }

}