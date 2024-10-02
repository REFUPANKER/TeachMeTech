package com.refupanker.teachmetech.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.refupanker.teachmetech.adapter.adapter_badges
import com.refupanker.teachmetech.databinding.FragmentProfileBinding
import com.refupanker.teachmetech.model.mdl_badge
import java.util.UUID


class FragmentProfile : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val badges: ArrayList<mdl_badge> = arrayListOf()
    private var adapter_badge: adapter_badges? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val ctx = requireContext()

        adapter_badge = adapter_badges(badges)
        binding.ProfileBadges.adapter = adapter_badge
        binding.ProfileBadges.layoutManager = GridLayoutManager(ctx, 8)


        GetBadges()

        return binding.root
    }

    fun GetBadges() {
        badges.clear()
        for (i in 0..4) {
            val token = UUID.randomUUID().toString()
            badges.add(
                mdl_badge(
                    token,
                    "My Proud Title",
                )
            )
        }
        badges.sortBy { m -> m.name }
        adapter_badge?.notifyDataSetChanged()
    }
}