package com.refupanker.teachmetech.model

import java.io.Serializable
import java.util.Date

data class mdl_announcement(
    val token: String,
    val title: String,
    val description: String,
    val date: Date? = Date(),
    val link: String? = null,
) : Serializable
