package com.refupanker.teachmetech.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.refupanker.teachmetech.databinding.ItemRowMessageBinding
import com.refupanker.teachmetech.model.mdl_chat_msg
import kotlin.random.Random

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

    private val nameColors = mutableMapOf<String, Int>()

    override fun onBindViewHolder(holder: holder, position: Int) {
        holder.binding.itemMsgSender.text = rows[position].sender
        holder.binding.itemMsgText.text = rows[position].message
        val nameIndex = nameColors.keys.indexOf(rows[position].sender)
        if (nameIndex != -1) {
            holder.binding.itemMsgSender.setTextColor(nameColors.getValue(rows[position].sender))
        } else {
            var max = 200
            var min = 100
            var r = 100
            var g = 100
            var b = 100
            when (Random.nextInt(0, 3)) {
                0 -> {
                    r = Random.nextInt(min, max)
                    g = Random.nextInt(min, min + (max - r))
                    b = Random.nextInt(min, min + (max - r))
                }

                1 -> {
                    g = Random.nextInt(min, max)
                    r = Random.nextInt(min, min + (max - g))
                    b = Random.nextInt(min, min + (max - g))
                }


                2 -> {
                    b = Random.nextInt(min, max)
                    r = Random.nextInt(min, min + (max - b))
                    g = Random.nextInt(min, min + (max - b))
                }
            }
            val newColor = Color.rgb(r, g, b)
            nameColors.put(rows[position].sender, newColor)
            holder.binding.itemMsgSender.setTextColor(newColor)
        }

    }
}