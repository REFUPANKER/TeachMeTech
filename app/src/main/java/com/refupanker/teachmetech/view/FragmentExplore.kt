package com.refupanker.teachmetech.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.refupanker.teachmetech.R
import com.refupanker.teachmetech.adapter.adapter_courses
import com.refupanker.teachmetech.adapter.adapter_leaderboard
import com.refupanker.teachmetech.databinding.FragmentExploreBinding
import com.refupanker.teachmetech.model.mdl_course
import com.refupanker.teachmetech.model.mdl_user
import java.util.Date
import java.util.UUID
import kotlin.random.Random

class FragmentExplore : Fragment() {
    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!

    private val leaderboard: ArrayList<mdl_user> = arrayListOf()
    private var adapter_leaderboardRows: adapter_leaderboard? = null


    private val courses: ArrayList<mdl_course> = arrayListOf()
    private var adapter_popularCourses: adapter_courses? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        val ctx = requireContext()

        adapter_popularCourses = adapter_courses(courses)
        binding.ExplorePopularCourses.adapter = adapter_popularCourses
        binding.ExplorePopularCourses.layoutManager = LinearLayoutManager(ctx)

        adapter_leaderboardRows = adapter_leaderboard(leaderboard)
        binding.ExploreLeaderboard.adapter = adapter_leaderboardRows
        binding.ExploreLeaderboard.layoutManager = LinearLayoutManager(ctx)

        GetLeaderboard()
        GetCourses()

        return binding.root
    }

    fun GetLeaderboard() {
        //TODO: rank naming guide
        leaderboard.clear()
        for (i in 0..4) {
            val token = UUID.randomUUID().toString()
            leaderboard.add(
                mdl_user(
                    token,
                    "Example user",
                    Random.nextInt(0, 1000)
                )
            )
        }
        leaderboard.sortByDescending { l -> l.rank }
        adapter_leaderboardRows?.notifyDataSetChanged()
    }

    fun GetCourses() {
        //TODO: first get course tokens order by like count then bring course data
        courses.clear()
        for (i in 0..4) {
            val token = UUID.randomUUID().toString()
            courses.add(
                mdl_course(
                    token,
                    "My Popular Course Title",
                    "Hello descr \n $token",
                    "General",
                    Date(),
                    Random.nextInt(0, 1000)
                )
            )
        }
        courses.sortedByDescending { c -> c.likes }

        adapter_popularCourses?.notifyDataSetChanged()
    }
}