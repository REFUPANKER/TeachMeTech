package com.refupanker.teachmetech.model

import java.io.Serializable

data class mdl_badge(
    val token: String,
    val name: String,
    val description: String = "Badge",

    ) : Serializable
