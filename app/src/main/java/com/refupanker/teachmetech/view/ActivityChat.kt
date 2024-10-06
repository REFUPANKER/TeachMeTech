package com.refupanker.teachmetech.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.refupanker.teachmetech.adapter.adapter_chat_messages
import com.refupanker.teachmetech.databinding.ActivityChatBinding
import com.refupanker.teachmetech.model.mdl_chat_msg
import com.refupanker.teachmetech.model.mdl_chatroom
import com.refupanker.teachmetech.model.mdl_user
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

class ActivityChat : AppCompatActivity() {

    private var _binding: ActivityChatBinding? = null
    private val binding get() = _binding!!

    private val messages: ArrayList<mdl_chat_msg> = arrayListOf()
    private var adapter_msgs: adapter_chat_messages? = null

    private var chatRoom: mdl_chatroom? = null

    val db = Firebase.database
    private val auth = Firebase.auth

    private var user: mdl_user? = null
    private var msgScore: Int = 0
    private var _rank: Long = 0

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

        //get user
        lifecycleScope.launch {
            Firebase.firestore.collection("Users")
                .document(auth.currentUser?.uid.toString()).get()
                .addOnCompleteListener { t ->
                    try {
                        if (t.isSuccessful) {
                            user = mdl_user(
                                token = t.result.data?.get("token") as String,
                                name = t.result.data!!["name"] as String,
                                rank = t.result.data!!["rank"] as Long,
                                active = t.result.data!!["active"] as Boolean
                            )
                            _rank = user!!.rank
                        } else {
                            Toast.makeText(
                                baseContext, "Cant get user data", Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: Exception) {

                    }
                }
        }

        //setup chat
        val chatNode = chatRoom?.token.toString()

        db.reference.child("ChatRooms").child(chatNode)
            .orderByChild("timestamp")
            .startAt(System.currentTimeMillis().toDouble())
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    lifecycleScope.launch {
                        try {
                            AddMessageItem(
                                mdl_chat_msg(
                                    snapshot.child("token").value.toString(),
                                    snapshot.child("user").value.toString(),
                                    snapshot.child("sender").value.toString(),
                                    snapshot.child("message").value.toString(),
                                )
                            )
                        } catch (e: Exception) {
                        }
                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    lifecycleScope.launch {
                        try {
                            AfterMessageDeleted(snapshot.key.toString())
                        } catch (e: Exception) {
                        }
                    }
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

                override fun onCancelled(error: DatabaseError) {}
            })

    }

    fun SendMessage() {
        if (user == null) {
            Toast.makeText(this, "Waiting data to receive", Toast.LENGTH_SHORT).show()
            return
        }
        var msg = binding.ChatInput.text.toString().trim()
            .replace("\n", " ")
            .replace("\u200E", "")

        if (msg.isNullOrEmpty() || msg.isBlank()) {
            Toast.makeText(this, "Message must be valid", Toast.LENGTH_SHORT).show()
            return
        }

        var msgToken = UUID.randomUUID().toString()
        db.reference
            .child("ChatRooms")
            .child(chatRoom?.token.toString())
            .child(msgToken)
            .setValue(
                mdl_chat_msg(
                    msgToken,
                    user!!.token,
                    user!!.name,
                    msg
                )
            )

        binding.ChatInput.text = null
        msgScore += 1

        if (msgScore % 5 == 0) {
            // add rank score
            _rank += 5
            lifecycleScope.launch {
                Firebase.firestore.collection("Users")
                    .document(auth.currentUser?.uid.toString())
                    .update("rank", _rank)
            }
            Toast.makeText(baseContext, "rank score increased !!", Toast.LENGTH_SHORT).show()
        }
        binding.ChatSendMsg.isEnabled = false
        binding.ChatInputCountDown.visibility = View.VISIBLE
        lifecycleScope.launch {
            for (i in 5 downTo 1) {
                binding.ChatInputCountDown.text = i.toString() + "s"
                delay(1000L)
            }
            binding.ChatInputCountDown.visibility = View.GONE
            binding.ChatSendMsg.isEnabled = true
        }
    }

    fun AddMessageItem(msg: mdl_chat_msg) {
        messages.add(msg)
        adapter_msgs?.notifyDataSetChanged()
        ScrollToBottom()
    }

    fun AfterMessageDeleted(token: String) {
        val index = messages.indexOfFirst { it.token == token }
        if (index != -1) {
            messages.removeAt(index)
            adapter_msgs?.notifyItemRemoved(index)
        }
        ScrollToBottom()
    }

    fun ScrollToBottom() {
        binding.ChatRecyclerView.smoothScrollToPosition(
            adapter_msgs?.itemCount?.minus(1) ?: 0
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}