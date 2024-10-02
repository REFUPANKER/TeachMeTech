package com.refupanker.teachmetech.view

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.refupanker.teachmetech.R
import com.refupanker.teachmetech.adapter.adapter_courses
import com.refupanker.teachmetech.databinding.FragmentCoursesBinding
import com.refupanker.teachmetech.model.mdl_course
import java.util.Date
import java.util.UUID
import kotlin.random.Random

class FragmentCourses : Fragment() {

    private var _binding: FragmentCoursesBinding? = null
    private val binding get() = _binding!!


    private val courses: ArrayList<mdl_course> = arrayListOf()
    private var adapter: adapter_courses? = null

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
            GetCourses()
        }

        binding.CoursesSearchBtn.setOnClickListener { MakeSearch() }
        binding.CoursesSearch.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                MakeSearch()
                true
            } else {
                false
            }
        }


        GetCourses()

        return binding.root
    }

    fun GetCourses() {
        courses.clear()
        for (i in 0..4) {
            val token = UUID.randomUUID().toString()
            courses.add(
                mdl_course(
                    token,
                    "My Course Title",
                    "Hello descr \n $token",
                    "General",
                    Date(),
                    Random.nextInt(0, 100)
                )
            )
        }

        when (binding.CoursesOrderBar.checkedRadioButtonId) {
            R.id.Courses_order_popularity -> courses.sortByDescending { c -> c.likes }
            R.id.Courses_order_newest -> courses.sortByDescending { c -> c.time }
            R.id.Courses_order_oldest -> courses.sortBy { c -> c.time }
        }

        adapter?.notifyDataSetChanged()
    }

    fun MakeSearch() {
        Toast.makeText(this.context, "Searching", Toast.LENGTH_SHORT).show()
    }
}