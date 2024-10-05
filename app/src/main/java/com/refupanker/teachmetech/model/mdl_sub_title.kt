package com.refupanker.teachmetech.model

import java.io.Serializable

data class mdl_sub_title(
    val type: String="title",
    val text: String,
    val size: Long,
) : Serializable
