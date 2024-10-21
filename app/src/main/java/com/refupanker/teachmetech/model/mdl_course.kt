package com.refupanker.teachmetech.model

import java.io.Serializable
import java.util.Date

data class mdl_course(
    val token: String,
    val title: String,
    val description: String,
    val category: String,
    val date: Date = Date(),
    val likes: Long = 0,
    val contents: ArrayList<String> = arrayListOf()
) : Serializable
