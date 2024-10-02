package com.refupanker.teachmetech.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.refupanker.teachmetech.databinding.ItemRowLeaderboardBinding
import com.refupanker.teachmetech.model.mdl_user

class adapter_leaderboard(private val rows: ArrayList<mdl_user>) :
    RecyclerView.Adapter<adapter_leaderboard.holder>() {

    class holder(val binding: ItemRowLeaderboardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): holder {
        var binding =
            ItemRowLeaderboardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return holder(binding)
    }

    override fun getItemCount(): Int {
        return rows.count()
    }

    override fun onBindViewHolder(holder: holder, position: Int) {
        holder.binding.root.contentDescription = rows[position].token
        holder.binding.itemRowLdrbrdUser.text = rows[position].name
        holder.binding.itemRowLdrbrdRank.text = rows[position].rank.toString()

        holder.binding.itemRowLdrbrdUser.setOnClickListener {
            Toast.makeText(
                holder.binding.root.context,
                "Show user profile by this token \n"+ holder.binding.root.contentDescription,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}