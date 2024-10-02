package com.refupanker.teachmetech.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.refupanker.teachmetech.databinding.ItemSlotBadgeBinding
import com.refupanker.teachmetech.model.mdl_badge

class adapter_badges(private val slots: ArrayList<mdl_badge>) :
    RecyclerView.Adapter<adapter_badges.holder>() {

    class holder(val binding: ItemSlotBadgeBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): holder {
        val binding =
            ItemSlotBadgeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return holder(binding)
    }

    override fun getItemCount(): Int {
        return slots.count()
    }

    override fun onBindViewHolder(holder: holder, position: Int) {
        holder.binding.root.contentDescription =
            slots[position].token + "\n" +
                    slots[position].name + "\n" +
                    slots[position].description

        holder.binding.root.setOnClickListener {
            Toast.makeText(
                holder.binding.root.context,
                holder.binding.root.contentDescription, Toast.LENGTH_SHORT
            ).show()
        }
    }
}