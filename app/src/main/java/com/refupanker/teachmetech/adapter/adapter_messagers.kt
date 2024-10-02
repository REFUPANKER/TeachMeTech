package com.refupanker.teachmetech.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.refupanker.teachmetech.databinding.ItemRowMessagersBinding
import com.refupanker.teachmetech.model.mdl_user

class adapter_messagers(private val rows: ArrayList<mdl_user>) :
    RecyclerView.Adapter<adapter_messagers.holder>() {
    class holder(val binding: ItemRowMessagersBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): holder {
        val binding =
            ItemRowMessagersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return holder(binding)
    }

    override fun getItemCount(): Int {
        return rows.count()
    }

    override fun onBindViewHolder(holder: holder, position: Int) {
        holder.binding.root.contentDescription = rows[position].token
        holder.binding.itemRowMsgrUser.text = rows[position].name

        holder.binding.root.setOnClickListener {
            Toast.makeText(
                holder.binding.root.context,
                "Displaying chat with\n" + rows[position].token,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}