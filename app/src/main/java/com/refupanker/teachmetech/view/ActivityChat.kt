package com.refupanker.teachmetech.view

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.refupanker.teachmetech.adapter.adapter_chat_messages
import com.refupanker.teachmetech.databinding.ActivityChatBinding
import com.refupanker.teachmetech.model.mdl_chat_msg
import com.refupanker.teachmetech.model.mdl_chatroom

class ActivityChat : AppCompatActivity() {

    private var _binding: ActivityChatBinding? = null
    private val binding get() = _binding!!

    private val messages: ArrayList<mdl_chat_msg> = arrayListOf()
    private var adapter_msgs: adapter_chat_messages? = null

    private var chatRoom: mdl_chatroom? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityChatBinding.inflate(layoutInflater) // Initialize _binding
        setContentView(binding.root)

        adapter_msgs = adapter_chat_messages(messages)
        binding.ChatRecyclerView.adapter = adapter_msgs
        binding.ChatRecyclerView.layoutManager = LinearLayoutManager(this)

        chatRoom = intent.getSerializableExtra("ChatRoom") as mdl_chatroom
        binding.ChatTitle.text = chatRoom?.name

        binding.ChatSendMsg.setOnClickListener { SendMessage() }
        binding.ChatGoBack.setOnClickListener { finish() }


        val chatNode = chatRoom?.token.toString()
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference()

        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.e("TMT", "Messages loaded from room: $chatNode")
                    for (messageSnapshot in dataSnapshot.children) {
                        Log.e("TMT", ">>>" + messageSnapshot.toString())
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("TMT", "Error: ${error.message}")
            }
        })

    }

    fun SendMessage() {
        AddMessageItem(mdl_chat_msg("token", "Admin", "Hello world!"))
    }

    fun AddMessageItem(msg: mdl_chat_msg) {
        messages.add(msg)
        adapter_msgs?.notifyDataSetChanged()
        binding.ChatRecyclerView.smoothScrollToPosition(
            adapter_msgs?.itemCount?.minus(1) ?: 0
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        //TODO: destroy listener on destroy
    }

}