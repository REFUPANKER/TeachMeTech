package com.refupanker.teachmetech.model

import java.io.Serializable
import java.util.Date

data class mdl_course_content(
    val data: ArrayList<Any>,
    val title: String,
    val time: Date = Date()
) : Serializable
