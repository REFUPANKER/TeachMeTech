package com.refupanker.teachmetech.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.refupanker.teachmetech.databinding.ItemRowDminviteBinding
import com.refupanker.teachmetech.model.mdl_dminvites

class adapter_dminvites(private val rows: ArrayList<mdl_dminvites>) :
    RecyclerView.Adapter<adapter_dminvites.holder>() {
    class holder(val binding: ItemRowDminviteBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): holder {
        val binding =
            ItemRowDminviteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return holder(binding)
    }

    override fun getItemCount(): Int {
        return rows.count()
    }

    override fun onBindViewHolder(holder: holder, position: Int) {
        holder.binding.itemRowDminvUser.text = rows[position].from
        //TODO: accept / reject systems
        holder.binding.itemRowDminvConfirm.setOnClickListener {
            Toast.makeText(
                holder.binding.root.context,
                "Invite Confirmed ?\n" + rows[position].from,
                Toast.LENGTH_SHORT
            ).show()
        }
        holder.binding.itemRowDminvReject.setOnClickListener {
            Toast.makeText(
                holder.binding.root.context,
                "Invite Rejected ?\n" + rows[position].from,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}