package com.refupanker.teachmetech.model

import java.io.Serializable

data class mdl_user(
    val token: String,
    val name: String,
    val email: String,
    val rank: Int,
    val active: Boolean = true

) : Serializable
