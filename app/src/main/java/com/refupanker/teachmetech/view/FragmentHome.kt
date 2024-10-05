package com.refupanker.teachmetech.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.collection.ArrayMap
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.refupanker.teachmetech.adapter.adapter_announcements
import com.refupanker.teachmetech.databinding.FragmentHomeBinding
import com.refupanker.teachmetech.model.mdl_announcement
import com.refupanker.teachmetech.model.mdl_course
import com.refupanker.teachmetech.model.mdl_sub_paragraph
import com.refupanker.teachmetech.model.mdl_sub_title
import com.refupanker.teachmetech.model.mdl_sub_video
import com.refupanker.teachmetech.model.mdl_user
import kotlinx.coroutines.launch
import java.util.UUID

class FragmentHome : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    public val binding get() = _binding!!

    private val annoncs: ArrayList<mdl_announcement> = arrayListOf()
    private var adapter_annoncs: adapter_announcements? = null

    private val db = Firebase.firestore
    private val auth = Firebase.auth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val ctx = requireContext()

        adapter_annoncs = adapter_announcements(annoncs)
        binding.HomeRecyclerViewAnnouncements.adapter = adapter_annoncs
        binding.HomeRecyclerViewAnnouncements.layoutManager = LinearLayoutManager(this.context)

        GetUserData()

        GetAnnoncs()

        return binding.root
    }

    fun AddAnnonc() {
        val token = UUID.randomUUID().toString()
        db.collection("Announcements").document(token).set(
            mdl_announcement(
                token,
                "Testing",
                "Hello world!\n" + token
            )
        ).addOnCompleteListener { t ->
            if (t.isSuccessful) {
                Toast.makeText(context, "new announcement added", Toast.LENGTH_SHORT).show()
            }
        }

    }

    //TODO: better user data view on UI
    private fun GetUserData() {
        binding.HomeUsername.text = "Loading ..."
        lifecycleScope.launch {
            Firebase.firestore.collection("Users")
                .document(auth.currentUser?.uid.toString()).get()
                .addOnCompleteListener { t ->
                    try {
                        if (t.isSuccessful) {
                            val user = mdl_user(
                                token = t.result.data?.get("token") as String,
                                name = t.result.data!!["name"] as String,
                                rank = t.result.data!!["rank"] as Long,
                                active = t.result.data!!["active"] as Boolean
                            )

                            binding.HomeUsername.text = user?.name
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Cant get user data",
                                Toast.LENGTH_SHORT
                            )
                                .show()

                        }
                    } catch (e: Exception) {

                    }
                }
        }
    }

    private fun GetAnnoncs() {
        viewLifecycleOwner.lifecycleScope.launch {
            annoncs.clear()
            binding.HomeAnnocsStatus.visibility = View.VISIBLE
            binding.HomeAnnocsStatus.text = "Loading ..."
            db.collection("Announcements")
                .orderBy("date", Query.Direction.DESCENDING)
                .limit(5)
                .get()
                .addOnCompleteListener { task ->
                    try {
                        if (task.isSuccessful) {
                            if (task.result.isEmpty) {
                                binding.HomeAnnocsStatus.text = "No data exists"
                            } else {
                                binding.HomeAnnocsStatus.visibility = View.GONE
                                for (i in task.result) {
                                    annoncs.add(
                                        mdl_announcement(
                                            token = i.getString("token").toString(),
                                            title = i.getString("title").toString(),
                                            description = i.getString("description").toString(),
                                            date = i.getDate("date"),
                                            link = i.getString("link")
                                        )
                                    )
                                }
                                adapter_annoncs?.notifyDataSetChanged()
                            }
                        } else {
                            binding.HomeAnnocsStatus.text = "Cant get announcements data"
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