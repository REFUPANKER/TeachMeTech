package com.refupanker.teachmetech.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.refupanker.teachmetech.databinding.ItemCardCourseBinding
import com.refupanker.teachmetech.model.mdl_course
import com.refupanker.teachmetech.view.ActivityCourse
import java.text.SimpleDateFormat

//TODO: optimize small card ui
class adapter_courses(private val courses: ArrayList<mdl_course>, isSmallCard: Boolean = false) :
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
        holder.binding.itemCourseLikes.text = courses[position].likes.toString()

        holder.binding.itemCourseTime.text =
            SimpleDateFormat("yyyy/MM/dd").format(courses[position].date)

        holder.binding.root.setOnClickListener {
            val courseIntent = Intent(holder.binding.root.context, ActivityCourse::class.java)
            courseIntent.putExtra("course", courses[position])
            holder.binding.root.context.startActivity(courseIntent)
        }
    }
}