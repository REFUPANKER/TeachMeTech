package com.refupanker.teachmetech.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.refupanker.teachmetech.R
import com.refupanker.teachmetech.adapter.adapter_dminvites
import com.refupanker.teachmetech.databinding.FragmentSubDmInvitesBinding
import com.refupanker.teachmetech.model.mdl_dminvites
import kotlinx.coroutines.launch

class FragmentSubDmInvites : Fragment() {

    private var _binding: FragmentSubDmInvitesBinding? = null
    private val binding get() = _binding!!

    private var dminvites: ArrayList<mdl_dminvites> = arrayListOf()
    private var adapter_dmi: adapter_dminvites? = null

    private val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSubDmInvitesBinding.inflate(inflater, container, false)

        val ctx = requireContext()
        adapter_dmi = adapter_dminvites(dminvites)
        binding.DmInvitesRecyclerView.adapter = adapter_dmi
        binding.DmInvitesRecyclerView.layoutManager = LinearLayoutManager(ctx)


        binding.DmInvitesGoBack.setOnClickListener {
            parentFragmentManager
                .beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_left,
                    R.anim.slide_out_right,
                )
                .replace(R.id.main_frame, FragmentMessages())
                .disallowAddToBackStack()
                .commit()
        }
        GetDmInvites()


        return binding.root
    }

    //TODO : move this thing to popup profile page
    fun AddDmInvite(user: String, target: String) {
        //TODO: improvement : you can add pre checking for both sided invite sendings
        //if both side tries to send invite , the latest one needs to check and set status accepted of invite
        //example ->     check(target,currentUser) == null > sendInvite / #dataExists > acceptInvite
        db.collection("DmInvites")
            .document(user + target).set(
                mdl_dminvites(
                    from = user,
                    to = target
                )
            )
    }

    fun GetDmInvites() {
        viewLifecycleOwner.lifecycleScope.launch {

            binding.DmInvitesStatus.visibility = View.VISIBLE
            binding.DmInvitesStatus.text = "Loading ..."
            val currentUserID = "GqlAGypmHBcHEIh0AdCmJlvjRt43"
            db.collection("DmInvites")
                .whereEqualTo("to", currentUserID)//TODO: change to current user uid
                .get().addOnCompleteListener { t ->
                    try {
                        if (t.isSuccessful) {
                            if (t.result.isEmpty) {
                                binding.DmInvitesStatus.text = "No data exists"
                            } else {
                                binding.DmInvitesStatus.visibility = View.GONE
                                for (i in t.result) {
                                    dminvites.add(
                                        mdl_dminvites(
                                            from = currentUserID,
                                            to = i.getString("to").toString()
                                        )
                                    )
                                }
                                adapter_dmi?.notifyDataSetChanged()
                            }
                        } else {
                            binding.DmInvitesStatus.text = "Cant get dm invites"
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