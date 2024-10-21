package com.refupanker.teachmetech.view

import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar.LayoutParams
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import androidx.lifecycle.lifecycleScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.refupanker.teachmetech.R
import com.refupanker.teachmetech.databinding.ActivityCourseBinding
import com.refupanker.teachmetech.model.mdl_course
import com.refupanker.teachmetech.model.mdl_course_content
import com.refupanker.teachmetech.model.mdl_sub_paragraph
import com.refupanker.teachmetech.model.mdl_sub_title
import com.refupanker.teachmetech.model.mdl_sub_video
import kotlinx.coroutines.launch
import java.util.Date

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

        binding.CourseStatus.visibility = View.VISIBLE
        binding.CourseStatus.text = "Loading ..."

        var i = 1
        lifecycleScope.launch {
            try {
                for (content in course.contents) {
                    db.collection("CourseContents")
                        .document(content).get()
                        .addOnCompleteListener { t ->
                            if (t.isSuccessful) {
                                val r = t.result
                                val mdl = mdl_course_content(
                                    data = r.get("data") as ArrayList<Any>,
                                    title = r.getString("title") as String,
                                    time = r.getDate("time") as Date
                                )

                                val contentRow = TextView(baseContext)
                                val layoutParams = LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                                )
                                layoutParams.setMargins(0, 0, 0, 16)
                                contentRow.layoutParams = layoutParams
                                contentRow.setTextColor(Color.WHITE)
                                contentRow.setBackgroundResource(R.drawable.style_box_rounded_gray)
                                contentRow.textSize = 20f
                                contentRow.setPadding(25)
                                contentRow.text = (i++).toString() + " > " + r.getString("title")

                                contentRow.setOnClickListener {
                                    val contentIntent =
                                        Intent(baseContext, ActivityCourseContent::class.java)
                                    contentIntent.putExtra("content", mdl)
                                    startActivity(contentIntent)
                                }
                                binding.CourseContentHolder.addView(contentRow)
                            } else {
                                Toast.makeText(
                                    baseContext,
                                    "Can't get content data",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            } catch (e: Exception) {
                Toast.makeText(
                    baseContext,
                    "Something went wrong,contact us",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.CourseStatus.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}