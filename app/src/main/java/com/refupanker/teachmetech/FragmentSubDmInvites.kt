package com.refupanker.teachmetech

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.refupanker.teachmetech.databinding.FragmentMessagesBinding
import com.refupanker.teachmetech.databinding.FragmentSubDmInvitesBinding
import com.refupanker.teachmetech.view.FragmentMessages

class FragmentSubDmInvites : Fragment() {

    private var _binding: FragmentSubDmInvitesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSubDmInvitesBinding.inflate(inflater, container, false)

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

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}