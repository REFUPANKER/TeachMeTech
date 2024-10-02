package com.refupanker.teachmetech.model

import java.io.Serializable
import java.util.Date

data class mdl_course(
    val token: String,
    val title: String,
    val description: String,
    val category: String,
    val time: Date,
    val likes: Int = 0,
) : Serializable
