package com.refupanker.teachmetech.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.refupanker.teachmetech.R
import com.refupanker.teachmetech.adapter.adapter_messagers
import com.refupanker.teachmetech.databinding.FragmentMessagesBinding
import com.refupanker.teachmetech.model.mdl_user
import java.util.UUID


class FragmentMessages : Fragment() {
    private var _binding: FragmentMessagesBinding? = null
    private val binding get() = _binding!!

    private var messagers: ArrayList<mdl_user> = arrayListOf()
    private var messagersFiltered: ArrayList<mdl_user> = arrayListOf()
    private var adapter_msgrs: adapter_messagers? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMessagesBinding.inflate(inflater, container, false)
        val ctx = requireContext()

        adapter_msgrs = adapter_messagers(messagers)
        binding.MessagesRecyclerView.adapter = adapter_msgrs
        binding.MessagesRecyclerView.layoutManager = LinearLayoutManager(ctx)

        binding.MessagesSearch.doAfterTextChanged { text ->
            if (!text.isNullOrEmpty()) {
                messagersFiltered.clear()
                messagersFiltered = messagers.filter { u ->
                    u.name.contains(binding.MessagesSearch.text.toString(), ignoreCase = true)
                } as ArrayList<mdl_user>
                adapter_msgrs = adapter_messagers(messagersFiltered)
                adapter_msgrs?.notifyDataSetChanged()
            } else {
                adapter_msgrs = adapter_messagers(messagers)
                adapter_msgrs?.notifyDataSetChanged()
            }
            binding.MessagesRecyclerView.adapter = adapter_msgrs
        }


        binding.MessagesNotifs.setOnClickListener {
            parentFragmentManager
                .beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left,
                )
                .replace(R.id.main_frame, FragmentSubDmInvites())
                .disallowAddToBackStack()
                .commit()
            parentFragmentManager.fragments.remove(this)
        }

        //GetMessagers()

        return binding.root
    }

    fun GetMessagers() {
        messagers.clear()
        for (i in 0..4) {
            val token = UUID.randomUUID().toString()
            messagers.add(
                mdl_user(
                    token,
                    "My Course Title $i",
                )
            )
        }
        messagers.sortBy { m -> m.name }
        adapter_msgrs?.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}