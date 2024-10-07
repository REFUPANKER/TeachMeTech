package com.refupanker.teachmetech.model

import java.io.Serializable

data class mdl_user(
    val token: String,
    val name: String,
    val rank: Long = 0,
    val active: Boolean = true,
    val aboutMe: String = "Hello im learning tech with teachmetech",
) : Serializable
