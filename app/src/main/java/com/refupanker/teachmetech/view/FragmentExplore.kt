package com.refupanker.teachmetech.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.refupanker.teachmetech.adapter.adapter_courses
import com.refupanker.teachmetech.adapter.adapter_leaderboard
import com.refupanker.teachmetech.databinding.FragmentExploreBinding
import com.refupanker.teachmetech.model.mdl_chatroom
import com.refupanker.teachmetech.model.mdl_course
import com.refupanker.teachmetech.model.mdl_user
import kotlinx.coroutines.launch
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

        binding.ExploreChatRoom1.setOnClickListener {
            val chatIntent = Intent(context, ActivityChat::class.java)
            chatIntent.putExtra(
                "ChatRoom", mdl_chatroom(
                    "PublicChatRooom1",
                    "public",
                    "Chat Room 1"
                )
            )
            startActivity(chatIntent)
        }



        GetLeaderboard()
        GetCourses()

        return binding.root
    }

    fun GetLeaderboard() {
        //TODO: improvement > rank naming guide
        viewLifecycleOwner.lifecycleScope.launch {
            leaderboard.clear()
            binding.ExploreLeaderboardStatus.visibility = View.VISIBLE
            binding.ExploreLeaderboardStatus.text = "Loading ..."
            db.collection("Users")
                .orderBy("rank", Query.Direction.DESCENDING)
                .limit(5).get()
                .addOnCompleteListener { task ->
                    try {
                        if (task.isSuccessful) {
                            if (task.result.isEmpty) {
                                binding.ExploreLeaderboardStatus.text = "No data exists"
                            } else {
                                binding.ExploreLeaderboardStatus.visibility = View.GONE
                                for (i in task.result) {
                                    leaderboard.add(
                                        mdl_user(
                                            token = i.getString("token") as String,
                                            name = i.getString("name") as String,
                                            rank = i.getLong("rank") as Long,
                                            active = i.getBoolean("active") as Boolean,
                                        )
                                    )
                                }
                                if (task.result.isEmpty) {
                                    binding.ExploreLeaderboardStatus.text = "No results found"
                                } else {
                                    binding.ExploreLeaderboardStatus.visibility = View.GONE
                                }
                                adapter_leaderboardRows?.notifyDataSetChanged()
                            }
                        } else {
                            binding.ExploreLeaderboardStatus.text = "Cant get leaderboard data"
                        }
                    } catch (e: Exception) {
                    }
                }


        }
    }

    fun GetCourses() {
        courses.clear()
        lifecycleScope.launch {
            binding.ExplorePopularCoursesdStatus.visibility = View.VISIBLE
            binding.ExplorePopularCoursesdStatus.text = "Loading ..."
            db.collection("Courses")
                .orderBy("likes", Query.Direction.DESCENDING)
                .limit(4)
                .get()
                .addOnCompleteListener { t ->
                    try {
                        if (t.isSuccessful) {
                            for (i in t.result) {
                                courses.add(
                                    mdl_course(
                                        token = i.getString("token").toString(),
                                        title = i.getString("title").toString(),
                                        description = i.getString("description").toString(),
                                        category = i.getString("category").toString(),
                                        date = i.getDate("date") as Date,
                                        likes = i.getLong("likes") as Long
                                    )
                                )
                            }
                            if (t.result.isEmpty) {
                                binding.ExplorePopularCoursesdStatus.text = "No results found"
                            } else {
                                binding.ExplorePopularCoursesdStatus.visibility = View.GONE
                            }
                            adapter_popularCourses?.notifyDataSetChanged()
                        } else {
                            Toast.makeText(context, "Cant get courses", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                    }
                }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}