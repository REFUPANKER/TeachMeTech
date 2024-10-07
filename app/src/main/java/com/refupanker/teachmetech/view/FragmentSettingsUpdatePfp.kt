package com.refupanker.teachmetech.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.refupanker.teachmetech.R
import com.refupanker.teachmetech.databinding.FragmentSettingsUpdatePfpBinding


class FragmentSettingsUpdatePfp : Fragment() {
    private var _binding: FragmentSettingsUpdatePfpBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsUpdatePfpBinding.inflate(inflater, container, false)
        val ctx = requireContext()

        binding.SettingsSubGoBack.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_out_right
                )
                .remove(this)
                .commit()
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}