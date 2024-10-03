package com.refupanker.teachmetech.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.refupanker.teachmetech.adapter.adapter_announcements
import com.refupanker.teachmetech.databinding.FragmentHomeBinding
import com.refupanker.teachmetech.model.mdl_announcement
import com.refupanker.teachmetech.model.mdl_user
import kotlinx.coroutines.launch
import java.util.UUID

class FragmentHome : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val annoncs: ArrayList<mdl_announcement> = arrayListOf()
    private var adapter_annoncs: adapter_announcements? = null


    private val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        adapter_annoncs = adapter_announcements(annoncs)
        binding.HomeRecyclerViewAnnouncements.adapter = adapter_annoncs
        binding.HomeRecyclerViewAnnouncements.layoutManager = LinearLayoutManager(this.context)

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

    private fun GetAnnoncs() {
        viewLifecycleOwner.lifecycleScope.launch {
            annoncs.clear()
            binding.HomeAnnocsStatus.visibility = View.VISIBLE
            binding.HomeAnnocsStatus.text = "Loading ..."
            db.collection("Announcements").orderBy("date", Query.Direction.DESCENDING).get()
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