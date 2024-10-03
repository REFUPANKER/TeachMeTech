package com.refupanker.teachmetech.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.refupanker.teachmetech.adapter.adapter_leaderboard.holder
import com.refupanker.teachmetech.databinding.ItemRowAnnouncementBinding
import com.refupanker.teachmetech.model.mdl_announcement
import java.text.SimpleDateFormat

class adapter_announcements(private val rows: ArrayList<mdl_announcement>) :
    RecyclerView.Adapter<adapter_announcements.holder>() {

    class holder(val binding: ItemRowAnnouncementBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): holder {
        val binding =
            ItemRowAnnouncementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return holder(binding)
    }

    override fun getItemCount(): Int {
        return rows.count()
    }

    override fun onBindViewHolder(holder: holder, position: Int) {
        holder.binding.announcTitle.text = rows[position].title
        holder.binding.announcDescr.text = rows[position].description
        holder.binding.announcDate.text = SimpleDateFormat("yyyy/MM/dd").format(rows[position].date)

        if (rows[position].link.isNullOrEmpty()) {
            holder.binding.announcLink.visibility = View.INVISIBLE
        }
        holder.binding.announcLink.setOnClickListener {
            Toast.makeText(
                holder.binding.root.context,
                "Opening link ...",
                Toast.LENGTH_SHORT
            ).show()
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(rows[position].link))
            ContextCompat.startActivity(holder.binding.root.context, intent, null)
        }

    }
}