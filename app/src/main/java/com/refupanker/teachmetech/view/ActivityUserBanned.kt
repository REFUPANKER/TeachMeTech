package com.refupanker.teachmetech.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.refupanker.teachmetech.databinding.ActivityUserBannedBinding
import com.refupanker.teachmetech.model.mdl_baninfo

class ActivityUserBanned : AppCompatActivity() {
    private var _binding: ActivityUserBannedBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityUserBannedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val baninfo = intent.getSerializableExtra("baninfo") as mdl_baninfo
        binding.userid.text = baninfo.token;
        binding.reason.text = baninfo.reason;
        binding.until.text = baninfo.until;

        binding.backtoauth.setOnClickListener {
            startActivity(Intent(this, ActivityAuth::class.java))
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}