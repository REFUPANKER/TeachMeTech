package com.refupanker.teachmetech.model

import java.io.Serializable

data class mdl_sub_link(
    val type: String="link",
    val text: String,
    val target: String,
) : Serializable
