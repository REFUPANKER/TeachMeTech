package com.refupanker.teachmetech.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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

    private val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        val ctx = requireContext()

        adapter_popularCourses = adapter_courses(courses)
        binding.ExplorePopularCourses.adapter = adapter_popularCourses
        binding.ExplorePopularCourses.layoutManager = GridLayoutManager(ctx, 2)

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
        binding.ExploreLeaderboardStatus.text = "Loading ..."
        db.collection("Users").orderBy("rank", Query.Direction.DESCENDING).limit(10).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (task.result.isEmpty) {
                        binding.ExploreLeaderboardStatus.text = "No data exists"
                    } else {
                        binding.ExploreLeaderboardStatus.text = ""
                        for (i in task.result) {
                            leaderboard.add(
                                mdl_user(
                                    token = i.getString("token").toString(),
                                    name = i.getString("name").toString(),
                                    rank = i.getLong("rank").toString().toInt(),
                                    active = i.getBoolean("active") == true,
                                )
                            )
                        }
                        adapter_leaderboardRows?.notifyDataSetChanged()
                    }
                } else {
                    binding.ExploreLeaderboardStatus.text = "Cant get leaderboard data"
                }
            }
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