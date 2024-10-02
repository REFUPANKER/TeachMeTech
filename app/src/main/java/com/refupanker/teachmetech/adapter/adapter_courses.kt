package com.refupanker.teachmetech.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.refupanker.teachmetech.databinding.ItemCardCourseBinding
import com.refupanker.teachmetech.model.mdl_course
import java.text.SimpleDateFormat

class adapter_courses(private val courses: ArrayList<mdl_course>) :
    RecyclerView.Adapter<adapter_courses.holder>() {

    class holder(val binding: ItemCardCourseBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): holder {
        val binding =
            ItemCardCourseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return holder(binding)
    }

    override fun getItemCount(): Int {
        return courses.count()
    }

    override fun onBindViewHolder(holder: holder, position: Int) {
        holder.binding.itemCourseTitle.text = courses[position].title
        holder.binding.itemCourseDescription.text = courses[position].description
        holder.binding.itemCourseCategory.text = courses[position].category

        //TODO: fetch thumbnail from storage with token

        //TODO: fetch likes
        holder.binding.itemCourseLikes.text = courses[position].likes.toString()

        holder.binding.itemCourseTime.text =
            SimpleDateFormat("yyyy/MM/dd").format(courses[position].time)
    }
}