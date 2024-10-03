package com.refupanker.teachmetech.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.refupanker.teachmetech.databinding.ItemRowMessageBinding
import com.refupanker.teachmetech.databinding.ItemRowMessagersBinding
import com.refupanker.teachmetech.model.mdl_chat_msg
import com.refupanker.teachmetech.model.mdl_user
import java.text.SimpleDateFormat

class adapter_chat_messages(private val rows: ArrayList<mdl_chat_msg>) :
    RecyclerView.Adapter<adapter_chat_messages.holder>() {
    class holder(val binding: ItemRowMessageBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): holder {
        val binding =
            ItemRowMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return holder(binding)
    }

    override fun getItemCount(): Int {
        return rows.count()
    }

    override fun onBindViewHolder(holder: holder, position: Int) {
        holder.binding.itemMsgSender.text = rows[position].sender
        holder.binding.itemMsgText.text = rows[position].message
    }
}