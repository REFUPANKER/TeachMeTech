package com.refupanker.teachmetech.model

import java.io.Serializable

data class mdl_dminvites(
    val from: String,
    val to: String,
    val status: String = "waiting",
) : Serializable
