package com.refupanker.teachmetech.view

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.getField
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase
import com.refupanker.teachmetech.R
import com.refupanker.teachmetech.adapter.adapter_courses
import com.refupanker.teachmetech.databinding.FragmentCoursesBinding
import com.refupanker.teachmetech.model.mdl_course
import com.refupanker.teachmetech.model.mdl_sub_paragraph
import com.refupanker.teachmetech.model.mdl_sub_title
import com.refupanker.teachmetech.model.mdl_sub_video
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

class FragmentCourses : Fragment() {

    private var _binding: FragmentCoursesBinding? = null
    private val binding get() = _binding!!


    private val courses: ArrayList<mdl_course> = arrayListOf()
    private var adapter: adapter_courses? = null

    private val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCoursesBinding.inflate(inflater, container, false)
        val ctx = requireContext()

        adapter = adapter_courses(courses)
        binding.CoursesRecyclerView.adapter = adapter
        binding.CoursesRecyclerView.layoutManager = LinearLayoutManager(ctx)

        binding.CoursesOrderBar.setOnCheckedChangeListener { group, checkedId ->
            when (binding.CoursesOrderBar.checkedRadioButtonId) {
                R.id.Courses_order_popularity -> courses.sortByDescending { c -> c.likes }
                R.id.Courses_order_newest -> courses.sortByDescending { c -> c.date }
                R.id.Courses_order_oldest -> courses.sortBy { c -> c.date }
            }
            adapter?.notifyDataSetChanged()
        }

        binding.CoursesSearchBtn.setOnClickListener { MakeSearch() }

        binding.CoursesClearSearch.setOnClickListener {
            binding.CoursesClearSearch.visibility = View.GONE
            binding.CoursesSearch.text = null
            binding.CoursesStatusText.text = null
            binding.CoursesStatusText.visibility = View.GONE
            GetCourses()
        }

        GetCourses()

        return binding.root
    }

    fun GetCourses() {
        courses.clear()
        lifecycleScope.launch {
            binding.CoursesStatusText.visibility = View.VISIBLE
            binding.CoursesStatusText.text = "Loading ..."
            db.collection("Courses")
                .orderBy("likes", Query.Direction.DESCENDING)
                .limit(5)
                .get()
                .addOnCompleteListener { t ->
                    try {
                        if (t.isSuccessful) {
                            for (i in t.result) {
                                courses.add(
                                    mdl_course(
                                        token = i.id,
                                        title = i.getString("title").toString(),
                                        description = i.getString("description").toString(),
                                        category = i.getString("category").toString(),
                                        date = i.getDate("date") as Date,
                                        likes = i.getLong("likes") as Long,
                                        contents = i.get("contents") as ArrayList<String>
                                    )
                                )
                            }
                            if (t.result.isEmpty) {
                                binding.CoursesStatusText.text = "No results found"
                            } else {
                                binding.CoursesStatusText.visibility = View.GONE
                            }
                            adapter?.notifyDataSetChanged()
                        } else {
                            Toast.makeText(context, "Cant get courses", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                    }
                }
        }
    }

    fun MakeSearch() {
        if (binding.CoursesSearch.text!!.length < 3) {
            Toast.makeText(
                this.context,
                "cant search short text (min 3 letter)",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        binding.CoursesClearSearch.visibility = View.VISIBLE
        binding.CoursesStatusText.visibility = View.VISIBLE
        binding.CoursesStatusText.text = "Searching ..."
        courses.clear()
        adapter?.notifyDataSetChanged()
        lifecycleScope.launch {
            val searchText = binding.CoursesSearch.text.toString()
            db.collection("Courses")
                .whereEqualTo("title", searchText.toCamelCaseSimple())
                .get().addOnSuccessListener { result ->
                    try {
                        if (result.isEmpty) {
                            binding.CoursesStatusText.text = "No courses found"
                        } else {
                            binding.CoursesStatusText.text = "${result.count()} courses found"
                            for (i in result.documents) {
                                courses.add(
                                    mdl_course(
                                        token = i.id,
                                        title = i.getString("title") as String,
                                        description = i.getString("description") as String,
                                        category = i.getString("category") as String,
                                        date = i.getDate("date") as Date,
                                        likes = i.getLong("likes") as Long,
                                        contents = i.get("contents") as ArrayList<String>
                                    )
                                )
                            }
                            adapter?.notifyDataSetChanged()
                        }
                    } catch (e: Exception) {
                        binding.CoursesStatusText.text = "An error occurred"
                    }
                }
        }
    }

    fun String.toCamelCaseSimple(): String {
        return split(" ")
            .joinToString(" ")
            { it.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() } }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}